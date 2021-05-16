package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.pledge.Clan;
import sf.l2j.gameserver.network.serverpackets.PledgePowerGradeList;

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
