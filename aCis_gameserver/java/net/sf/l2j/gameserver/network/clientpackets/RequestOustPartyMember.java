package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.finex.enums.EPartyMessageType;

public final class RequestOustPartyMember extends L2GameClientPacket {

	private String _name;

	@Override
	protected void readImpl() {
		_name = readS();
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		final Party party = player.getParty();
		if (party == null || !party.isLeader(player)) {
			return;
		}

		party.removePartyMember(_name, EPartyMessageType.EXPELLED);
	}
}
