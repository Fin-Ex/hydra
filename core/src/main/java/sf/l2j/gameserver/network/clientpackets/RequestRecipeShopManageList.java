package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;

public final class RequestRecipeShopManageList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		player.tryOpenWorkshop(true);
	}
}
