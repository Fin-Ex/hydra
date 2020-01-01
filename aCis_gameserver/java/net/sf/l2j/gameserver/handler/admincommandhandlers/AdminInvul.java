package net.sf.l2j.gameserver.handler.admincommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles following admin commands: - invul = turns invulnerability
 * on/off
 */
public class AdminInvul implements IAdminCommandHandler {

	private static Logger _log = LoggerFactory.getLogger(AdminInvul.class.getName());
	private static final String[] ADMIN_COMMANDS = {
		"admin_invul",
		"admin_setinvul"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		if (command.equals("admin_invul")) {
			handleInvul(activeChar);
		}
		if (command.equals("admin_setinvul")) {
			WorldObject target = activeChar.getTarget();
			if (target instanceof Player) {
				handleInvul((Player) target);
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList() {
		return ADMIN_COMMANDS;
	}

	private static void handleInvul(Player activeChar) {
		String text;
		if (activeChar.isInvul()) {
			activeChar.setIsInvul(false);
			text = activeChar.getName() + " is now mortal.";
			if (Config.DEBUG) {
				_log.info("GM: Gm removed invul mode from character " + activeChar.getName() + "(" + activeChar.getObjectId() + ")");
			}
		} else {
			activeChar.setIsInvul(true);
			text = activeChar.getName() + " is now invulnerable.";
			if (Config.DEBUG) {
				_log.info("GM: Gm activated invul mode for character " + activeChar.getName() + "(" + activeChar.getObjectId() + ")");
			}
		}
		activeChar.sendMessage(text);
	}
}
