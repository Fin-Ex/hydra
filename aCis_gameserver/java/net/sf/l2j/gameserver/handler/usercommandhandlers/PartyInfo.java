package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class PartyInfo implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		81
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player player = (Player) args[1];
		final Party party = player.getParty();
		if (party == null) {
			return;
		}

		player.sendPacket(SystemMessageId.PARTY_INFORMATION);
		player.sendPacket(party.getLootRule().getMessageId());
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PARTY_LEADER_S1).addString(party.getLeader().getName()));
		player.sendMessage("Members: " + party.getMembersCount() + "/9");
		player.sendPacket(SystemMessageId.FRIEND_LIST_FOOTER);
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
