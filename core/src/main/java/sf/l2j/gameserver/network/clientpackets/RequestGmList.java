package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.data.xml.AdminData;
import sf.l2j.gameserver.model.actor.Player;

public final class RequestGmList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		AdminData.getInstance().sendListToPlayer(activeChar);
	}
}
