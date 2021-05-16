package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Player;
import sf.finex.enums.EStoreType;
import sf.l2j.gameserver.network.serverpackets.RecipeShopItemInfo;

public final class RequestRecipeShopMakeInfo extends L2GameClientPacket {

	private int _playerObjectId, _recipeId;

	@Override
	protected void readImpl() {
		_playerObjectId = readD();
		_recipeId = readD();
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		final Player shop = World.getInstance().getPlayer(_playerObjectId);
		if (shop == null || shop.getStoreType() != EStoreType.MANUFACTURE) {
			return;
		}

		player.sendPacket(new RecipeShopItemInfo(shop, _recipeId));
	}
}
