package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;

public class RequestUserCommand extends L2GameClientPacket {

	private int _command;

	@Override
	protected void readImpl() {
		_command = readD();
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		final IHandler handler = HandlerTable.getInstance().get(_command);
		if (handler != null) {
			handler.invoke(_command, getClient().getActiveChar());
		}
	}
}
