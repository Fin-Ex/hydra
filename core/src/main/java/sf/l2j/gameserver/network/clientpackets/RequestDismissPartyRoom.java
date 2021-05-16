package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.partymatching.PartyMatchRoom;
import sf.l2j.gameserver.model.partymatching.PartyMatchRoomList;

public class RequestDismissPartyRoom extends L2GameClientPacket {

	private int _roomid;
	@SuppressWarnings("unused")
	private int _data2;

	@Override
	protected void readImpl() {
		_roomid = readD();
		_data2 = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		PartyMatchRoom room = PartyMatchRoomList.getInstance().getRoom(_roomid);
		if (room == null) {
			return;
		}

		PartyMatchRoomList.getInstance().deleteRoom(_roomid);
	}
}
