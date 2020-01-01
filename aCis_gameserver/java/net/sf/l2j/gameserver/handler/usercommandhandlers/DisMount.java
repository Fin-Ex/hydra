package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * Support for /dismount command.
 *
 * @author Micht
 */
public class DisMount implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		62
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		if (activeChar.isMounted()) {
			activeChar.dismount();
		}
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
