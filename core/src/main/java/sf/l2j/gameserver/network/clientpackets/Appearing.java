package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.UserInfo;

public final class Appearing extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.isTeleporting()) {
			activeChar.onTeleported();
		}

		sendPacket(new UserInfo(activeChar));
	}

	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
