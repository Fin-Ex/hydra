package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.pledge.Clan;
import sf.l2j.gameserver.model.pledge.ClanMember;
import sf.l2j.gameserver.network.serverpackets.PledgeReceivePowerInfo;

/**
 * Format: (ch) dS
 *
 * @author -Wooden-
 */
public final class RequestPledgeMemberPowerInfo extends L2GameClientPacket {

	@SuppressWarnings("unused")
	private int _pledgeType;
	private String _player;

	@Override
	protected void readImpl() {
		_pledgeType = readD();
		_player = readS();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final Clan clan = activeChar.getClan();
		if (clan == null) {
			return;
		}

		final ClanMember member = clan.getClanMember(_player);
		if (member == null) {
			return;
		}

		activeChar.sendPacket(new PledgeReceivePowerInfo(member));
	}
}
