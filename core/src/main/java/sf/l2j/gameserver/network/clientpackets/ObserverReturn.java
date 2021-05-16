package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;

public final class ObserverReturn extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.isInObserverMode()) {
			activeChar.leaveObserverMode();
		}
	}
}
