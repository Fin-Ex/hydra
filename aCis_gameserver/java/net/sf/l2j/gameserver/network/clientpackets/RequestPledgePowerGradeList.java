package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.serverpackets.PledgePowerGradeList;

/**
 * Format: (ch)
 *
 * @author -Wooden-
 */
public final class RequestPledgePowerGradeList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		final Clan clan = player.getClan();
		if (clan == null) {
			return;
		}

		player.sendPacket(new PledgePowerGradeList(clan.getAllRankPrivs(), clan.getMembers()));
	}
}
