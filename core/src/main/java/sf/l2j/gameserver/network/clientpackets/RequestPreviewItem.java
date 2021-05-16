package sf.l2j.gameserver.network.clientpackets;

import java.util.HashMap;
import java.util.Map;

import sf.l2j.commons.concurrent.ThreadPool;

import sf.l2j.Config;
import sf.l2j.gameserver.data.manager.BuyListManager;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.instance.Merchant;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.buylist.NpcBuyList;
import sf.l2j.gameserver.model.buylist.Product;
import sf.l2j.gameserver.model.item.kind.Item;
import sf.l2j.gameserver.model.itemcontainer.Inventory;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.ShopPreviewInfo;
import sf.l2j.gameserver.network.serverpackets.UserInfo;

public final class RequestPreviewItem extends L2GameClientPacket {

	private Map<Integer, Integer> _itemList;
	@SuppressWarnings("unused")
	private int _unk;
	private int _listId;
	private int _count;
	private int[] _items;

	private class RemoveWearItemsTask implements Runnable {

		private final Player activeChar;

		protected RemoveWearItemsTask(Player player) {
			activeChar = player;
		}

		@Override
		public void run() {
			try {
				activeChar.sendPacket(SystemMessageId.NO_LONGER_TRYING_ON);
				activeChar.sendPacket(new UserInfo(activeChar));
			} catch (Exception e) {
				_log.error("", e);
			}
		}
	}

	@Override
	protected void readImpl() {
		_unk = readD();
		_listId = readD();
		_count = readD();

		if (_count < 0) {
			_count = 0;
		} else if (_count > 100) {
			return; // prevent too long lists
		}
		// Create _items table that will contain all ItemID to Wear
		_items = new int[_count];

		// Fill _items table with all ItemID to Wear
		for (int i = 0; i < _count; i++) {
			_items[i] = readD();
		}
	}

	@Override
	protected void runImpl() {
		if (_items == null) {
			return;
		}

		if (_count < 1 || _listId >= 4000000) {
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// Get the current player and return if null
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		// Check current target of the player and the INTERACTION_DISTANCE
		WorldObject target = activeChar.getTarget();
		if (!activeChar.isGM() && (target == null || !(target instanceof Merchant) || !activeChar.isInsideRadius(target, Npc.INTERACTION_DISTANCE, false, false))) {
			return;
		}

		// Get the current merchant targeted by the player
		final Merchant merchant = (target instanceof Merchant) ? (Merchant) target : null;
		if (merchant == null) {
			_log.warn(getClass().getName() + " Null merchant!");
			return;
		}

		final NpcBuyList buyList = BuyListManager.getInstance().getBuyList(_listId);
		if (buyList == null) {
			return;
		}

		int totalPrice = 0;
		_listId = buyList.getListId();
		_itemList = new HashMap<>();

		for (int i = 0; i < _count; i++) {
			int itemId = _items[i];

			final Product product = buyList.getProductByItemId(itemId);
			if (product == null) {
				return;
			}

			final Item template = product.getItem();
			if (template == null) {
				continue;
			}

			final int slot = Inventory.getPaperdollIndex(template.getBodyPart());
			if (slot < 0) {
				continue;
			}

			if (_itemList.containsKey(slot)) {
				activeChar.sendPacket(SystemMessageId.YOU_CAN_NOT_TRY_THOSE_ITEMS_ON_AT_THE_SAME_TIME);
				return;
			}
			_itemList.put(slot, itemId);

			totalPrice += Config.WEAR_PRICE;
			if (totalPrice > Integer.MAX_VALUE) {
				return;
			}
		}

		// Charge buyer and add tax to castle treasury if not owned by npc clan because a Try On is not Free
		if (totalPrice < 0 || !activeChar.reduceAdena("Wear", totalPrice, activeChar.getCurrentFolkNPC(), true)) {
			activeChar.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
			return;
		}

		if (!_itemList.isEmpty()) {
			activeChar.sendPacket(new ShopPreviewInfo(_itemList));

			// Schedule task
			ThreadPool.schedule(new RemoveWearItemsTask(activeChar), Config.WEAR_DELAY * 1000);
		}
	}
}
