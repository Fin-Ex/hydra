package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.CommandChannel;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class ChannelLeave implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		96
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player player = (Player) args[1];
		final Party party = player.getParty();
		if (party == null || !party.isLeader(player)) {
			return;
		}

		final CommandChannel channel = party.getCommandChannel();
		if (channel == null) {
			return;
		}

		channel.removeParty(party);

		party.broadcastMessage(SystemMessageId.LEFT_COMMAND_CHANNEL);
		channel.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PARTY_LEFT_COMMAND_CHANNEL).addCharName(player));
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
