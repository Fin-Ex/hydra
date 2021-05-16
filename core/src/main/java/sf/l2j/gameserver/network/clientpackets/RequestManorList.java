package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.ExSendManorList;

/**
 * Format: ch
 *
 * @author l3x
 */
public class RequestManorList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		player.sendPacket(ExSendManorList.STATIC_PACKET);
	}
}
