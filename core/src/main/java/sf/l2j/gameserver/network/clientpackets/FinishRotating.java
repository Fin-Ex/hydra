package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.StopRotation;

public final class FinishRotating extends L2GameClientPacket {

	private int _degree;
	@SuppressWarnings("unused")
	private int _unknown;

	@Override
	protected void readImpl() {
		_degree = readD();
		_unknown = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		activeChar.broadcastPacket(new StopRotation(activeChar.getObjectId(), _degree, 0));
	}
}
