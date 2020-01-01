package net.sf.l2j.gameserver.network.clientpackets;

import java.util.StringTokenizer;
import net.sf.finex.data.tables.TalentBranchTable;
import net.sf.finex.model.talents.LineageCommandHandler;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.OlympiadManagerNpc;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestBypassToServer extends L2GameClientPacket {

	private static final Logger GMAUDIT_LOG = LoggerFactory.getLogger("gmaudit");

	private String _command;

	@Override
	protected void readImpl() {
		_command = readS();
	}

	@Override
	protected void runImpl() {
		if (!FloodProtectors.performAction(getClient(), Action.SERVER_BYPASS)) {
			return;
		}

		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (_command.isEmpty()) {
			_log.info(activeChar.getName() + " sent an empty requestBypass packet.");
			activeChar.logout();
			return;
		}

		try {
			if (_command.startsWith("admin_")) {
				String command = _command.split(" ")[0];

				IAdminCommandHandler ach = AdminCommandHandler.getInstance().getAdminCommandHandler(command);
				if (ach == null) {
					if (activeChar.isGM()) {
						activeChar.sendMessage("The command " + command.substring(6) + " doesn't exist.");
					}

					_log.warn("No handler registered for admin command '" + command + "'");
					return;
				}

				if (!AdminData.getInstance().hasAccess(command, activeChar.getAccessLevel())) {
					activeChar.sendMessage("You don't have the access rights to use this command.");
					_log.warn(activeChar.getName() + " tried to use admin command " + command + " without proper Access Level.");
					return;
				}

				if (Config.GMAUDIT) {
					GMAUDIT_LOG.info(activeChar.getName() + " [" + activeChar.getObjectId() + "] used '" + _command + "' command on: " + ((activeChar.getTarget() != null) ? activeChar.getTarget().getName() : "none"));
				}

				ach.useAdminCommand(_command, activeChar);
			} else if (_command.startsWith("player_help ")) {
				playerHelp(activeChar, _command.substring(12));
			} else if (_command.startsWith("npc_")) {
				if (!activeChar.validateBypass(_command)) {
					return;
				}

				int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0) {
					id = _command.substring(4, endOfId);
				} else {
					id = _command.substring(4);
				}

				try {
					final WorldObject object = World.getInstance().getObject(Integer.parseInt(id));

					if (object != null && object instanceof Npc && endOfId > 0 && ((Npc) object).canInteract(activeChar)) {
						((Npc) object).onBypassFeedback(activeChar, _command.substring(endOfId + 1));
					}

					activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				} catch (NumberFormatException nfe) {
				}
			} // Navigate throught Manor windows
			else if (_command.startsWith("manor_menu_select?")) {
				WorldObject object = activeChar.getTarget();
				if (object instanceof Npc) {
					((Npc) object).onBypassFeedback(activeChar, _command);
				}
			} else if (_command.startsWith("bbs_") || _command.startsWith("_bbs") || _command.startsWith("_friend") || _command.startsWith("_mail") || _command.startsWith("_block")) {
				CommunityBoard.getInstance().handleCommands(getClient(), _command);
			} else if (_command.startsWith("Quest ")) {
				if (!activeChar.validateBypass(_command)) {
					return;
				}

				String[] str = _command.substring(6).trim().split(" ", 2);
				if (str.length == 1) {
					activeChar.processQuestEvent(str[0], "");
				} else {
					activeChar.processQuestEvent(str[0], str[1]);
				}
			} else if (_command.startsWith("_match")) {
				String params = _command.substring(_command.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0) {
					Hero.getInstance().showHeroFights(activeChar, heroclass, heroid, heropage);
				}
			} else if (_command.startsWith("_diary")) {
				String params = _command.substring(_command.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0) {
					Hero.getInstance().showHeroDiary(activeChar, heroclass, heroid, heropage);
				}
			} else if (_command.startsWith("arenachange")) // change
			{
				final boolean isManager = activeChar.getCurrentFolkNPC() instanceof OlympiadManagerNpc;
				if (!isManager) {
					// Without npc, command can be used only in observer mode on arena
					if (!activeChar.isInObserverMode() || activeChar.isInOlympiadMode() || activeChar.getOlympiadGameId() < 0) {
						return;
					}
				}

				if (OlympiadManager.getInstance().isRegisteredInComp(activeChar)) {
					activeChar.sendPacket(SystemMessageId.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME);
					return;
				}

				final int arenaId = Integer.parseInt(_command.substring(12).trim());
				activeChar.enterOlympiadObserverMode(arenaId);
			} else if (_command.startsWith("usercmd")) {
				final StringTokenizer st = new StringTokenizer(_command, " ");
				st.nextToken();
				final int cmdId = Integer.parseInt(st.nextToken());
				HandlerTable.getInstance().get(cmdId).invoke(cmdId, activeChar);
			} else if (_command.startsWith("talent")) {
				final StringTokenizer st = new StringTokenizer(_command, " ");
				String mainCmd = st.nextToken();

				if (mainCmd.equalsIgnoreCase("talentInfo")) {
					final int talentId = Integer.parseInt(st.nextToken());
					if (TalentBranchTable.getInstance().checkBranch(activeChar)) {
						LineageCommandHandler.showTalentInfo(activeChar, talentId);
					}
				} else if (mainCmd.equalsIgnoreCase("talentLearn")) {
					final int talentId = Integer.parseInt(st.nextToken());
					LineageCommandHandler.buttonTalentLearn(activeChar, talentId);
				} else if (mainCmd.equalsIgnoreCase("talentList")) {
					LineageCommandHandler.showTalentList(activeChar);
				} else if (mainCmd.equalsIgnoreCase("talentReset")) {
					LineageCommandHandler.buttonResetTalents(activeChar);
				} else {
					throw new UnsupportedOperationException("Command " + mainCmd + " not handled in " + getClass().getCanonicalName());
				}
			}
		} catch (NumberFormatException e) {
			_log.warn("Bad RequestBypassToServer: " + e, e);
		}
	}

	private static void playerHelp(Player activeChar, String path) {
		if (path.indexOf("..") != -1) {
			return;
		}

		final StringTokenizer st = new StringTokenizer(path);
		final String[] cmd = st.nextToken().split("#");

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/help/" + cmd[0]);
		if (cmd.length > 1) {
			html.setItemId(Integer.parseInt(cmd[1]));
		}
		html.disableValidation();
		activeChar.sendPacket(html);
	}
}
