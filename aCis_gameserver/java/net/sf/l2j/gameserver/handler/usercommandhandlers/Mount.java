package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * Support for /mount command.
 *
 * @author Tempy
 */
public class Mount implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		61
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		activeChar.mountPlayer(activeChar.getActiveSummon());
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
