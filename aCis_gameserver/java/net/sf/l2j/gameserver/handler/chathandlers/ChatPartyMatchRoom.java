package net.sf.l2j.gameserver.handler.chathandlers;


import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.partymatching.PartyMatchRoom;
import net.sf.l2j.gameserver.model.partymatching.PartyMatchRoomList;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatPartyMatchRoom implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		14
	};

	@Override
	public void invoke(Object... args) {
		final int type = (int) args[0];
		final Player activeChar = (Player) args[1];
		final String params = (String) args[2];
		final String text = (String) args[3];
		if (!activeChar.isInPartyMatchRoom()) {
			return;
		}

		final PartyMatchRoom room = PartyMatchRoomList.getInstance().getPlayerRoom(activeChar);
		if (room == null) {
			return;
		}

		final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
		for (Player member : room.getPartyMembers()) {
			member.sendPacket(cs);
		}
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
