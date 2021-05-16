package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;

public final class RequestDeleteMacro extends L2GameClientPacket {

	private int _id;

	@Override
	protected void readImpl() {
		_id = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		activeChar.deleteMacro(_id);
	}
}
