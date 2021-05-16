package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;

public final class RequestRecipeShopManageQuit extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		player.forceStandUp();
		player.broadcastUserInfo();
	}
}
