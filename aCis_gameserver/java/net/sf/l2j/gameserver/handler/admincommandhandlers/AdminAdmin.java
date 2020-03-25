package net.sf.l2j.gameserver.handler.admincommandhandlers;


import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.Config;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.data.DoorTable;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.data.xml.AnnouncementData;
import net.sf.l2j.gameserver.data.xml.MultisellData;
import net.sf.l2j.gameserver.data.xml.TeleportLocationData;
import net.sf.l2j.gameserver.data.xml.WalkerRouteData;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.StartRotation;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;

/**
 * This class handles following admin commands:
 * <ul>
 * <li>admin/admin1/admin2/admin3/admin4 : the different admin menus.</li>
 * <li>gmlist : includes/excludes active character from /gmlist results.</li>
 * <li>kill : handles the kill command.</li>
 * <li>silence : toggles private messages acceptance mode.</li>
 * <li>tradeoff : toggles trade acceptance mode.</li>
 * <li>reload : reloads specified component.</li>
 * <li>script_load : loads following script. MUSTN'T be used instead of //reload
 * quest !</li>
 * </ul>
 */
@Slf4j
public class AdminAdmin implements IAdminCommandHandler {

	private static final String[] ADMIN_COMMANDS = {
		"admin_admin",
		"admin_admin1",
		"admin_admin2",
		"admin_admin3",
		"admin_admin4",
		"admin_gmlist",
		"admin_kill",
		"admin_silence",
		"admin_tradeoff",
		"admin_reload",
		"admin_headto"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		if (command.startsWith("admin_admin")) {
			showMainPage(activeChar, command);
		} else if (command.startsWith("admin_gmlist")) {
			activeChar.sendMessage((AdminData.getInstance().showOrHideGm(activeChar)) ? "Removed from GMList." : "Registered into GMList.");
		} else if (command.startsWith("admin_kill")) {
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken(); // skip command

			if (!st.hasMoreTokens()) {
				final WorldObject obj = activeChar.getTarget();
				if (!(obj instanceof Creature)) {
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				} else {
					kill(activeChar, (Creature) obj);
				}

				return true;
			}

			String firstParam = st.nextToken();
			Player player = World.getInstance().getPlayer(firstParam);
			if (player != null) {
				if (st.hasMoreTokens()) {
					String secondParam = st.nextToken();
					if (StringUtil.isDigit(secondParam)) {
						int radius = Integer.parseInt(secondParam);
						for (Creature knownChar : player.getKnownTypeInRadius(Creature.class, radius)) {
							if (knownChar.equals(activeChar)) {
								continue;
							}

							kill(activeChar, knownChar);
						}
						activeChar.sendMessage("Killed all characters within a " + radius + " unit radius around " + player.getName() + ".");
					} else {
						activeChar.sendMessage("Invalid radius.");
					}
				} else {
					kill(activeChar, player);
				}
			} else if (StringUtil.isDigit(firstParam)) {
				int radius = Integer.parseInt(firstParam);
				for (Creature knownChar : activeChar.getKnownTypeInRadius(Creature.class, radius)) {
					kill(activeChar, knownChar);
				}

				activeChar.sendMessage("Killed all characters within a " + radius + " unit radius.");
			}
		} else if (command.startsWith("admin_silence")) {
			if (activeChar.isInRefusalMode()) // already in message refusal mode
			{
				activeChar.setInRefusalMode(false);
				activeChar.sendPacket(SystemMessageId.MESSAGE_ACCEPTANCE_MODE);
			} else {
				activeChar.setInRefusalMode(true);
				activeChar.sendPacket(SystemMessageId.MESSAGE_REFUSAL_MODE);
			}
		} else if (command.startsWith("admin_tradeoff")) {
			try {
				String mode = command.substring(15);
				if (mode.equalsIgnoreCase("on")) {
					activeChar.setTradeRefusal(true);
					activeChar.sendMessage("Trade refusal enabled");
				} else if (mode.equalsIgnoreCase("off")) {
					activeChar.setTradeRefusal(false);
					activeChar.sendMessage("Trade refusal disabled");
				}
			} catch (Exception e) {
				if (activeChar.getTradeRefusal()) {
					activeChar.setTradeRefusal(false);
					activeChar.sendMessage("Trade refusal disabled");
				} else {
					activeChar.setTradeRefusal(true);
					activeChar.sendMessage("Trade refusal enabled");
				}
			}
		} else if (command.startsWith("admin_reload")) {
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			try {
				do {
					String type = st.nextToken();
					if (type.startsWith("admin")) {
						AdminData.getInstance().reload();
						activeChar.sendMessage("Admin data has been reloaded.");
					} else if (type.startsWith("announcement")) {
						AnnouncementData.getInstance().reload();
						activeChar.sendMessage("The content of announcements.xml has been reloaded.");
					} else if (type.startsWith("config")) {
						Config.loadGameServer();
						activeChar.sendMessage("Configs files have been reloaded.");
					} else if (type.startsWith("crest")) {
						CrestCache.getInstance().reload();
						activeChar.sendMessage("Crests have been reloaded.");
					} else if (type.startsWith("cw")) {
						CursedWeaponsManager.getInstance().reload();
						activeChar.sendMessage("Cursed weapons have been reloaded.");
					} else if (type.startsWith("door")) {
						DoorTable.getInstance().reload();
						activeChar.sendMessage("Doors instance has been reloaded.");
					} else if (type.startsWith("htm")) {
						HtmCache.getInstance().reload();
						activeChar.sendMessage("The HTM cache has been reloaded.");
					} else if (type.startsWith("item")) {
						ItemTable.getInstance().reload();
						activeChar.sendMessage("Items' templates have been reloaded.");
					} else if (type.equals("multisell")) {
						MultisellData.getInstance().reload();
						activeChar.sendMessage("The multisell instance has been reloaded.");
					} else if (type.equals("npc")) {
						NpcTable.getInstance().reloadAllNpc();
						activeChar.sendMessage("NPCs templates have been reloaded.");
					} else if (type.startsWith("npcwalker")) {
						WalkerRouteData.getInstance().reload();
						activeChar.sendMessage("Walker routes have been reloaded.");
					} else if (type.startsWith("skill")) {
						SkillTable.getInstance().reload();
						activeChar.sendMessage("Skills' XMLs have been reloaded.");
					} else if (type.startsWith("teleport")) {
						TeleportLocationData.getInstance().reload();
						activeChar.sendMessage("Teleport locations have been reloaded.");
					} else if (type.startsWith("zone")) {
						ZoneManager.getInstance().reload();
						activeChar.sendMessage("Zones have been reloaded.");
					} else {
						activeChar.sendMessage("Usage : //reload <acar|announcement|config|crest|door>");
						activeChar.sendMessage("Usage : //reload <htm|item|multisell|npc|npcwalker>");
						activeChar.sendMessage("Usage : //reload <skill|teleport|zone>");
					}
				} while (st.hasMoreTokens());
			} catch (Exception e) {
				activeChar.sendMessage("Usage : //reload <acar|announcement|config|crest|door>");
				activeChar.sendMessage("Usage : //reload <htm|item|multisell|npc|npcwalker>");
				activeChar.sendMessage("Usage : //reload <skill|teleport|zone>");
			}
		} else if (command.equalsIgnoreCase("admin_headto")) {
			final WorldObject target = activeChar.getTarget();
			if(target == null) {
				activeChar.broadcastPacket(new StartRotation(activeChar.getObjectId(), Short.MAX_VALUE));
				activeChar.broadcastPacket(new StopRotation(activeChar.getObjectId(), 0));
				activeChar.setHeading(0);
				activeChar.sendMessage("Can't use this command cause need real target (not self).");
				return false;
			}
			final int newHead = MathUtil.calculateHeadingFrom(activeChar, target);
			activeChar.broadcastPacket(new StartRotation(activeChar.getObjectId(), target.getCreature().getHeading()));
			activeChar.broadcastPacket(new StopRotation(activeChar.getObjectId(), newHead));
			activeChar.setHeading(newHead);
		} else if (command.startsWith("admin_social")) {
			final WorldObject target = activeChar.getTarget();
			if(target == null) {
				activeChar.sendMessage("Can't use this command cause need real target.");
				return false;
			}
			
			final StringTokenizer st = new StringTokenizer(command, " ");
			try {
				st.nextToken();
				if (target.isPlayer()) {
					target.getPlayer().broadcastPacket(new SocialAction(target.getPlayer(), Integer.valueOf(st.nextToken())));
				} else {
					target.getNpc().broadcastPacket(new SocialAction(target.getCreature(), Integer.valueOf(st.nextToken())));
				}
			} catch (NumberFormatException e) {
				log.error("Wrong command use. Use the command [social_id from 1 to 15]");
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList() {
		return ADMIN_COMMANDS;
	}

	private static void kill(Player activeChar, Creature target) {
		if (target instanceof Player) {
			if (!((Player) target).isGM()) {
				target.stopAllEffects(); // e.g. invincibility effect
			}
			target.reduceCurrentHp(target.getMaxHp() + target.getMaxCp() + 1, activeChar, null);
		} else if (target.isChampion()) {
			target.reduceCurrentHp(target.getMaxHp() * Config.CHAMPION_HP + 1, activeChar, null);
		} else {
			target.reduceCurrentHp(target.getMaxHp() + 1, activeChar, null);
		}
	}

	private static void showMainPage(Player activeChar, String command) {
		int mode = 0;
		String filename = null;
		try {
			mode = Integer.parseInt(command.substring(11));
		} catch (Exception e) {
		}

		switch (mode) {
			case 1:
				filename = "main";
				break;
			case 2:
				filename = "game";
				break;
			case 3:
				filename = "effects";
				break;
			case 4:
				filename = "server";
				break;
			default:
				filename = "main";
				break;
		}
		AdminHelpPage.showHelpPage(activeChar, filename + "_menu.htm");
	}
}
