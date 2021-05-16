package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.group.Party;
import sf.finex.enums.EPartyMessageType;
import sf.l2j.gameserver.model.partymatching.PartyMatchRoom;
import sf.l2j.gameserver.model.partymatching.PartyMatchRoomList;
import sf.l2j.gameserver.network.serverpackets.ExClosePartyRoom;
import sf.l2j.gameserver.network.serverpackets.ExPartyRoomMember;
import sf.l2j.gameserver.network.serverpackets.PartyMatchDetail;

public final class RequestWithdrawParty extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		final Party party = player.getParty();
		if (party == null) {
			return;
		}

		party.removePartyMember(player, EPartyMessageType.LEFT);

		if (player.isInPartyMatchRoom()) {
			PartyMatchRoom room = PartyMatchRoomList.getInstance().getPlayerRoom(player);
			if (room != null) {
				player.sendPacket(new PartyMatchDetail(room));
				player.sendPacket(new ExPartyRoomMember(room, 0));
				player.sendPacket(ExClosePartyRoom.STATIC_PACKET);

				room.deleteMember(player);
			}
			player.setPartyRoom(0);
			player.broadcastUserInfo();
		}
	}
}
