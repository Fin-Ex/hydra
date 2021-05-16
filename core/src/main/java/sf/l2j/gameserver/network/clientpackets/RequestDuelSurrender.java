package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.instancemanager.DuelManager;

/**
 * Format:(ch)
 *
 * @author -Wooden-
 */
public final class RequestDuelSurrender extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		DuelManager.getInstance().doSurrender(getClient().getActiveChar());
	}
}
