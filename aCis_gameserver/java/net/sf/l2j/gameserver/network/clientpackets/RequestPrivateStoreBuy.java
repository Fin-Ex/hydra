package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.ItemRequest;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.finex.enums.EStoreType;
import net.sf.l2j.gameserver.model.tradelist.TradeList;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestPrivateStoreBuy extends L2GameClientPacket {

	private static final int BATCH_LENGTH = 12; // length of one item

	private int _storePlayerId;
	private Set<ItemRequest> _items = null;

	@Override
	protected void readImpl() {
		_storePlayerId = readD();
		int count = readD();
		if (count <= 0 || count > Config.MAX_ITEM_IN_PACKET || count * BATCH_LENGTH != _buf.remaining()) {
			return;
		}

		_items = new HashSet<>();

		for (int i = 0; i < count; i++) {
			int objectId = readD();
			long cnt = readD();
			int price = readD();

			if (objectId < 1 || cnt < 1 || price < 0) {
				_items = null;
				return;
			}

			_items.add(new ItemRequest(objectId, (int) cnt, price));
		}
	}

	@Override
	protected void runImpl() {
		Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		if (_items == null) {
			return;
		}

		Player storePlayer = World.getInstance().getPlayer(_storePlayerId);
		if (storePlayer == null) {
			return;
		}

		if (player.isCursedWeaponEquipped()) {
			return;
		}

		if (!player.isInsideRadius(storePlayer, 150, true, false)) {
			return;
		}

		if (!(storePlayer.getStoreType() == EStoreType.SELL || storePlayer.getStoreType() == EStoreType.PACKAGE_SELL)) {
			return;
		}

		TradeList storeList = storePlayer.getSellList();
		if (storeList == null) {
			return;
		}

		if (!player.getAccessLevel().allowTransaction()) {
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}

		if (storePlayer.getStoreType() == EStoreType.PACKAGE_SELL && storeList.getItems().size() > _items.size()) {
			return;
		}

		int result = storeList.privateStoreBuy(player, _items);
		if (result > 0) {
			if (result > 1) {
				_log.warn("PrivateStore buy has failed due to invalid list or request. Player: " + player.getName() + ", Private store of: " + storePlayer.getName());
			}
			return;
		}

		if (storeList.getItems().isEmpty()) {
			storePlayer.setStoreType(EStoreType.NONE);
			storePlayer.broadcastUserInfo();
		}
	}
}
