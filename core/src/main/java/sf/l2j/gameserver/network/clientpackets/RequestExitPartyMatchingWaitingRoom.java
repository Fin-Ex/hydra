package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.partymatching.PartyMatchWaitingList;

public final class RequestExitPartyMatchingWaitingRoom extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		PartyMatchWaitingList.getInstance().removePlayer(activeChar);
	}
}
