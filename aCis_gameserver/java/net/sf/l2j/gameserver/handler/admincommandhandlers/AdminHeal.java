package net.sf.l2j.gameserver.handler.admincommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles following admin commands: - heal = restores HP/MP/CP on
 * target, name or radius
 */
public class AdminHeal implements IAdminCommandHandler {

	private static Logger _log = LoggerFactory.getLogger(AdminHeal.class.getName());
	private static final String[] ADMIN_COMMANDS = {
		"admin_heal"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		if (command.equals("admin_heal")) {
			handleRes(activeChar);
		} else if (command.startsWith("admin_heal")) {
			try {
				String healTarget = command.substring(11);
				handleRes(activeChar, healTarget);
			} catch (StringIndexOutOfBoundsException e) {
				if (Config.DEVELOPER) {
					System.out.println("Heal error: " + e);
				}
				activeChar.sendMessage("Incorrect target/radius specified.");
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList() {
		return ADMIN_COMMANDS;
	}

	private static void handleRes(Player activeChar) {
		handleRes(activeChar, null);
	}

	private static void handleRes(Player activeChar, String player) {
		WorldObject obj = activeChar.getTarget();
		if (player != null) {
			Player plyr = World.getInstance().getPlayer(player);

			if (plyr != null) {
				obj = plyr;
			} else {
				try {
					int radius = Integer.parseInt(player);
					for (Creature character : activeChar.getKnownType(Creature.class)) {
						character.setCurrentHpMp(character.getMaxHp(), character.getMaxMp());
						if (character instanceof Player) {
							character.setCurrentCp(character.getMaxCp());
						}
					}
					activeChar.sendMessage("Healed within " + radius + " unit radius.");
					return;
				} catch (NumberFormatException nbe) {
				}
			}
		}

		if (obj == null) {
			obj = activeChar;
		}

		if (obj instanceof Creature) {
			Creature target = (Creature) obj;
			target.setCurrentHpMp(target.getMaxHp(), target.getMaxMp());

			if (target instanceof Player) {
				target.setCurrentCp(target.getMaxCp());
			}

			if (Config.DEBUG) {
				_log.info("GM: " + activeChar.getName() + "(" + activeChar.getObjectId() + ") healed character " + target.getName());
			}
		} else {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
		}
	}
}
