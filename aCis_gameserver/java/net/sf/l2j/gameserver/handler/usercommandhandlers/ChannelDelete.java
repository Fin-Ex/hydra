package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.CommandChannel;
import net.sf.l2j.gameserver.model.group.Party;

public class ChannelDelete implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		93
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
		if (channel == null || !channel.isLeader(player)) {
			return;
		}

		channel.disband();
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
