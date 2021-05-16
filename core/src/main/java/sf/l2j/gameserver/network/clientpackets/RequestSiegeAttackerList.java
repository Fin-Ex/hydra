package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.instancemanager.CastleManager;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.entity.Castle;
import sf.l2j.gameserver.network.serverpackets.SiegeAttackerList;

public final class RequestSiegeAttackerList extends L2GameClientPacket {

	private int _castleId;

	@Override
	protected void readImpl() {
		_castleId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null) {
			return;
		}

		sendPacket(new SiegeAttackerList(castle));
	}
}
