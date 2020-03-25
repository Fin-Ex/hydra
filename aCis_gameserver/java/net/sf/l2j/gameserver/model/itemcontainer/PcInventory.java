package net.sf.l2j.gameserver.model.itemcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.sf.finex.dao.ItemDao;
import net.sf.finex.model.GLT.GLTArbitrator;
import net.sf.finex.model.talents.effects.HeavyGrip;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.EItemLocation;
import net.sf.l2j.gameserver.model.item.instance.type.HunterCardInstance;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.item.type.ItemType;
import net.sf.l2j.gameserver.model.itemcontainer.listeners.ArmorSetListener;
import net.sf.l2j.gameserver.model.itemcontainer.listeners.BowRodListener;
import net.sf.l2j.gameserver.model.itemcontainer.listeners.ItemPassiveSkillsListener;
import net.sf.l2j.gameserver.model.tradelist.TradeItem;
import net.sf.l2j.gameserver.model.tradelist.TradeList;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.taskmanager.ShadowItemTaskManager;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

public class PcInventory extends Inventory {

	public static final int ADENA_ID = 57;
	public static final int ANCIENT_ADENA_ID = 5575;

	private final Player _owner;
	@Getter @Setter private ItemInstance adenaInstance;
	@Getter @Setter private ItemInstance ancientAdenaInstance;
	@Getter @Setter private HunterCardInstance hunterCardInstance;

	public PcInventory(Player owner) {
		super();
		_owner = owner;
		addPaperdollListener(ArmorSetListener.getInstance());
		addPaperdollListener(BowRodListener.getInstance());
		addPaperdollListener(ItemPassiveSkillsListener.getInstance());
		addPaperdollListener(ShadowItemTaskManager.getInstance());
	}

	@Override
	public Player getOwner() {
		return _owner;
	}

	@Override
	protected EItemLocation getBaseLocation() {
		return EItemLocation.INVENTORY;
	}

	@Override
	protected EItemLocation getEquipLocation() {
		return EItemLocation.PAPERDOLL;
	}

	@Override
	public int getAdena() {
		return adenaInstance != null ? adenaInstance.getCount() : 0;
	}

	public int getAncientAdena() {
		return (ancientAdenaInstance != null) ? ancientAdenaInstance.getCount() : 0;
	}

	public ItemInstance[] getUniqueItems(boolean allowAdena, boolean allowAncientAdena) {
		return getUniqueItems(allowAdena, allowAncientAdena, true);
	}

	/**
	 * Returns the list of items in inventory available for transaction
	 *
	 * @param allowAdena
	 * @param allowAncientAdena
	 * @param onlyAvailable
	 * @return ItemInstance : items in inventory
	 */
	public ItemInstance[] getUniqueItems(boolean allowAdena, boolean allowAncientAdena, boolean onlyAvailable) {
		List<ItemInstance> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item == null) {
				continue;
			}

			if (!allowAdena && item.getItemId() == ADENA_ID) {
				continue;
			}

			if (!allowAncientAdena && item.getItemId() == ANCIENT_ADENA_ID) {
				continue;
			}

			boolean isDuplicate = false;
			for (ItemInstance litem : list) {
				if (litem.getItemId() == item.getItemId()) {
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate && (!onlyAvailable || (item.isSellable() && item.isAvailable(getOwner(), false, false)))) {
				list.add(item);
			}
		}
		return list.toArray(new ItemInstance[list.size()]);
	}

	/**
	 * Returns the list of items in inventory available for transaction Allows
	 * an item to appear twice if and only if there is a difference in
	 * enchantment level.
	 *
	 * @param allowAdena
	 * @param allowAncientAdena
	 * @return ItemInstance : items in inventory
	 */
	public ItemInstance[] getUniqueItemsByEnchantLevel(boolean allowAdena, boolean allowAncientAdena) {
		return getUniqueItemsByEnchantLevel(allowAdena, allowAncientAdena, true);
	}

	public ItemInstance[] getUniqueItemsByEnchantLevel(boolean allowAdena, boolean allowAncientAdena, boolean onlyAvailable) {
		List<ItemInstance> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item == null) {
				continue;
			}

			if (!allowAdena && item.getItemId() == ADENA_ID) {
				continue;
			}

			if (!allowAncientAdena && item.getItemId() == ANCIENT_ADENA_ID) {
				continue;
			}

			boolean isDuplicate = false;
			for (ItemInstance litem : list) {
				if ((litem.getItemId() == item.getItemId()) && (litem.getEnchantLevel() == item.getEnchantLevel())) {
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate && (!onlyAvailable || (item.isSellable() && item.isAvailable(getOwner(), false, false)))) {
				list.add(item);
			}
		}
		return list.toArray(new ItemInstance[list.size()]);
	}

	/**
	 * @param itemId
	 * @return
	 * @see
	 * net.sf.l2j.gameserver.model.itemcontainer.PcInventory#getAllItemsByItemId(int,
	 * boolean)
	 */
	public ItemInstance[] getAllItemsByItemId(int itemId) {
		return getAllItemsByItemId(itemId, true);
	}

	/**
	 * Returns the list of all items in inventory that have a given item id.
	 *
	 * @param itemId : ID of item
	 * @param includeEquipped : include equipped items
	 * @return ItemInstance[] : matching items from inventory
	 */
	public ItemInstance[] getAllItemsByItemId(int itemId, boolean includeEquipped) {
		List<ItemInstance> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item == null) {
				continue;
			}

			if (item.getItemId() == itemId && (includeEquipped || !item.isEquipped())) {
				list.add(item);
			}
		}
		return list.toArray(new ItemInstance[list.size()]);
	}

	/**
	 * @param itemId
	 * @param enchantment
	 * @return
	 * @see
	 * net.sf.l2j.gameserver.model.itemcontainer.PcInventory#getAllItemsByItemId(int,
	 * int, boolean)
	 */
	public ItemInstance[] getAllItemsByItemId(int itemId, int enchantment) {
		return getAllItemsByItemId(itemId, enchantment, true);
	}

	/**
	 * Returns the list of all items in inventory that have a given item id AND
	 * a given enchantment level.
	 *
	 * @param itemId : ID of item
	 * @param enchantment : enchant level of item
	 * @param includeEquipped : include equipped items
	 * @return ItemInstance[] : matching items from inventory
	 */
	public ItemInstance[] getAllItemsByItemId(int itemId, int enchantment, boolean includeEquipped) {
		List<ItemInstance> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item == null) {
				continue;
			}

			if ((item.getItemId() == itemId) && (item.getEnchantLevel() == enchantment) && (includeEquipped || !item.isEquipped())) {
				list.add(item);
			}
		}
		return list.toArray(new ItemInstance[list.size()]);
	}

	/**
	 * Returns the list of items in inventory available for transaction
	 *
	 * @param allowAdena
	 * @param allowNonTradeable
	 * @return ItemInstance : items in inventory
	 */
	public ItemInstance[] getAvailableItems(boolean allowAdena, boolean allowNonTradeable) {
		List<ItemInstance> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item != null && item.isAvailable(getOwner(), allowAdena, allowNonTradeable)) {
				list.add(item);
			}
		}
		return list.toArray(new ItemInstance[list.size()]);
	}

	/**
	 * @return a List of all sellable items.
	 */
	public List<ItemInstance> getSellableItems() {
		return _items.stream().filter(i -> !i.isEquipped() && i.isSellable() && (getOwner().getActiveSummon() == null || i.getObjectId() != getOwner().getActiveSummon().getControlItemId())).collect(Collectors.toList());
	}

	/**
	 * Get all augmented items
	 *
	 * @return
	 */
	public ItemInstance[] getAugmentedItems() {
		List<ItemInstance> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item != null && item.isAugmented()) {
				list.add(item);
			}
		}
		return list.toArray(new ItemInstance[list.size()]);
	}

	/**
	 * Returns the list of items in inventory available for transaction adjusetd
	 * by tradeList
	 *
	 * @param tradeList
	 * @return ItemInstance : items in inventory
	 */
	public TradeItem[] getAvailableItems(TradeList tradeList) {
		List<TradeItem> list = new ArrayList<>();
		for (ItemInstance item : _items) {
			if (item != null && item.isAvailable(getOwner(), false, false)) {
				TradeItem adjItem = tradeList.adjustAvailableItem(item);
				if (adjItem != null) {
					list.add(adjItem);
				}
			}
		}
		return list.toArray(new TradeItem[list.size()]);
	}

	/**
	 * Adjust TradeItem according his status in inventory
	 *
	 * @param item : ItemInstance to be adjusten
	 */
	public void adjustAvailableItem(TradeItem item) {
		boolean notAllEquipped = false;
		for (ItemInstance adjItem : getItemsByItemId(item.getItem().getItemId())) {
			if (adjItem.isEquipable()) {
				if (!adjItem.isEquipped()) {
					notAllEquipped |= true;
				}
			} else {
				notAllEquipped |= true;
				break;
			}
		}
		if (notAllEquipped) {
			ItemInstance adjItem = getItemByItemId(item.getItem().getItemId());
			item.setObjectId(adjItem.getObjectId());
			item.setEnchant(adjItem.getEnchantLevel());

			if (adjItem.getCount() < item.getCount()) {
				item.setCount(adjItem.getCount());
			}

			return;
		}

		item.setCount(0);
	}

	/**
	 * Adds adena to PCInventory
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param count : int Quantity of adena to be added
	 * @param actor : Player Player requesting the item add
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 */
	public void addAdena(String process, int count, Player actor, WorldObject reference) {
		if (count > 0) {
			addItem(process, ADENA_ID, count, actor, reference);
		}
	}

	/**
	 * Removes adena to PCInventory
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param count : int Quantity of adena to be removed
	 * @param actor : Player Player requesting the item add
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return true if successful.
	 */
	public boolean reduceAdena(String process, int count, Player actor, WorldObject reference) {
		if (count > 0) {
			return destroyItemByItemId(process, ADENA_ID, count, actor, reference) != null;
		}

		return false;
	}

	/**
	 * Adds specified amount of ancient adena to player inventory.
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param count : int Quantity of adena to be added
	 * @param actor : Player Player requesting the item add
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 */
	public void addAncientAdena(String process, int count, Player actor, WorldObject reference) {
		if (count > 0) {
			addItem(process, ANCIENT_ADENA_ID, count, actor, reference);
		}
	}

	/**
	 * Removes specified amount of ancient adena from player inventory.
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param count : int Quantity of adena to be removed
	 * @param actor : Player Player requesting the item add
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return true if successful.
	 */
	public boolean reduceAncientAdena(String process, int count, Player actor, WorldObject reference) {
		if (count > 0) {
			return destroyItemByItemId(process, ANCIENT_ADENA_ID, count, actor, reference) != null;
		}

		return false;
	}

	/**
	 * Adds item in inventory and checks _adena and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param item : ItemInstance to be added
	 * @param actor : Player Player requesting the item add
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the new item or the updated item in
	 * inventory
	 */
	@Override
	public ItemInstance addItem(String process, ItemInstance item, Player actor, WorldObject reference) {
		item = super.addItem(process, item, actor, reference);
		if (item == null) {
			return null;
		}

		if (item.getItemId() == ADENA_ID && !item.equals(adenaInstance)) {
			adenaInstance = item;
		} else if (item.getItemId() == ANCIENT_ADENA_ID && !item.equals(ancientAdenaInstance)) {
			ancientAdenaInstance = item;
		} else if(item.getItemId() == GLTArbitrator.HUNTER_CARD_ID) {
			hunterCardInstance = (HunterCardInstance) item;
			hunterCardInstance.refresh(actor);
		}

		return item;
	}

	/**
	 * Adds item in inventory and checks _adena and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item Identifier of the item to be added
	 * @param count : int Quantity of items to be added
	 * @param actor : Player Player requesting the item creation
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the new item or the updated item in
	 * inventory
	 */
	@Override
	public ItemInstance addItem(String process, int itemId, int count, Player actor, WorldObject reference) {
		ItemInstance item = super.addItem(process, itemId, count, actor, reference);
		if (item == null) {
			return null;
		}

		if (item.getItemId() == ADENA_ID && !item.equals(adenaInstance)) {
			adenaInstance = item;
		} else if (item.getItemId() == ANCIENT_ADENA_ID && !item.equals(ancientAdenaInstance)) {
			ancientAdenaInstance = item;
		}

		if (actor != null) {
			// Send inventory update packet
			InventoryUpdate playerIU = new InventoryUpdate();
			playerIU.addItem(item);
			actor.sendPacket(playerIU);

			// Update current load as well
			StatusUpdate su = new StatusUpdate(actor);
			su.addAttribute(StatusUpdate.CUR_LOAD, actor.getCurrentLoad());
			actor.sendPacket(su);
		}

		return item;
	}

	@Override
	public ItemInstance transferItem(String process, int objectId, int count, ItemContainer target, Player actor, WorldObject reference) {
		ItemInstance item = super.transferItem(process, objectId, count, target, actor, reference);

		if (adenaInstance != null && (adenaInstance.getCount() <= 0 || adenaInstance.getOwnerId() != getOwnerId())) {
			adenaInstance = null;
		}

		if (ancientAdenaInstance != null && (ancientAdenaInstance.getCount() <= 0 || ancientAdenaInstance.getOwnerId() != getOwnerId())) {
			ancientAdenaInstance = null;
		}

		return item;
	}

	/**
	 * Destroy item from inventory and checks _adena and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param item : ItemInstance to be destroyed
	 * @param actor : Player Player requesting the item destroy
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the destroyed item or the updated
	 * item in inventory
	 */
	@Override
	public ItemInstance destroyItem(String process, ItemInstance item, Player actor, WorldObject reference) {
		return this.destroyItem(process, item, item.getCount(), actor, reference);
	}

	/**
	 * Destroy item from inventory and checks _adena and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param item : ItemInstance to be destroyed
	 * @param actor : Player Player requesting the item destroy
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the destroyed item or the updated
	 * item in inventory
	 */
	@Override
	public ItemInstance destroyItem(String process, ItemInstance item, int count, Player actor, WorldObject reference) {
		item = super.destroyItem(process, item, count, actor, reference);

		if (adenaInstance != null && adenaInstance.getCount() <= 0) {
			adenaInstance = null;
		}

		if (ancientAdenaInstance != null && ancientAdenaInstance.getCount() <= 0) {
			ancientAdenaInstance = null;
		}

		return item;
	}

	/**
	 * Destroys item from inventory and checks _adena and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param objectId : int Item Instance identifier of the item to be
	 * destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param actor : Player Player requesting the item destroy
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the destroyed item or the updated
	 * item in inventory
	 */
	@Override
	public ItemInstance destroyItem(String process, int objectId, int count, Player actor, WorldObject reference) {
		ItemInstance item = getItemByObjectId(objectId);
		if (item == null) {
			return null;
		}

		return this.destroyItem(process, item, count, actor, reference);
	}

	/**
	 * Destroy item from inventory by using its <B>itemId</B> and checks _adena
	 * and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param actor : Player Player requesting the item destroy
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the destroyed item or the updated
	 * item in inventory
	 */
	@Override
	public ItemInstance destroyItemByItemId(String process, int itemId, int count, Player actor, WorldObject reference) {
		ItemInstance item = getItemByItemId(itemId);
		if (item == null) {
			return null;
		}

		return this.destroyItem(process, item, count, actor, reference);
	}

	/**
	 * Drop item from inventory and checks _adena and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param item : ItemInstance to be dropped
	 * @param actor : Player Player requesting the item drop
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the destroyed item or the updated
	 * item in inventory
	 */
	@Override
	public ItemInstance dropItem(String process, ItemInstance item, Player actor, WorldObject reference) {
		item = super.dropItem(process, item, actor, reference);

		if (adenaInstance != null && (adenaInstance.getCount() <= 0 || adenaInstance.getOwnerId() != getOwnerId())) {
			adenaInstance = null;
		}

		if (ancientAdenaInstance != null && (ancientAdenaInstance.getCount() <= 0 || ancientAdenaInstance.getOwnerId() != getOwnerId())) {
			ancientAdenaInstance = null;
		}

		return item;
	}

	/**
	 * Drop item from inventory by using its <B>objectID</B> and checks _adena
	 * and _ancientAdena
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param objectId : int Item Instance identifier of the item to be dropped
	 * @param count : int Quantity of items to be dropped
	 * @param actor : Player Player requesting the item drop
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the destroyed item or the updated
	 * item in inventory
	 */
	@Override
	public ItemInstance dropItem(String process, int objectId, int count, Player actor, WorldObject reference) {
		ItemInstance item = super.dropItem(process, objectId, count, actor, reference);

		if (adenaInstance != null && (adenaInstance.getCount() <= 0 || adenaInstance.getOwnerId() != getOwnerId())) {
			adenaInstance = null;
		}

		if (ancientAdenaInstance != null && (ancientAdenaInstance.getCount() <= 0 || ancientAdenaInstance.getOwnerId() != getOwnerId())) {
			ancientAdenaInstance = null;
		}

		return item;
	}

	/**
	 * <b>Overloaded</b>, when removes item from inventory, remove also owner
	 * shortcuts.
	 *
	 * @param item : ItemInstance to be removed from inventory
	 */
	@Override
	protected boolean removeItem(ItemInstance item) {
		// Removes any reference to the item from Shortcut bar
		getOwner().removeItemFromShortCut(item.getObjectId());

		// Removes active Enchant Scroll
		if (item.equals(getOwner().getActiveEnchantItem())) {
			getOwner().setActiveEnchantItem(null);
		}

		if (item.getItemId() == ADENA_ID) {
			adenaInstance = null;
		} else if (item.getItemId() == ANCIENT_ADENA_ID) {
			ancientAdenaInstance = null;
		}

		return super.removeItem(item);
	}

	/**
	 * Refresh the weight of equipment loaded
	 */
	@Override
	public void refreshWeight() {
		super.refreshWeight();
		getOwner().refreshOverloaded();
	}

	/**
	 * Get back items in inventory from database
	 */
	@Override
	public void restore() {
		super.restore();
		adenaInstance = getItemByItemId(ADENA_ID);
		ancientAdenaInstance = getItemByItemId(ANCIENT_ADENA_ID);
	}

	public static int[][] restoreVisibleInventory(int objectId) {
		int[][] paperdoll = new int[0x12][3];
		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement2 = con.prepareStatement("SELECT object_id,item_id,loc_data,enchant_level FROM items WHERE owner_id=? AND loc='PAPERDOLL'");
			statement2.setInt(1, objectId);
			ResultSet invdata = statement2.executeQuery();

			while (invdata.next()) {
				int slot = invdata.getInt("loc_data");
				paperdoll[slot][0] = invdata.getInt("object_id");
				paperdoll[slot][1] = invdata.getInt("item_id");
				paperdoll[slot][2] = invdata.getInt("enchant_level");
			}

			invdata.close();
			statement2.close();
		} catch (Exception e) {
			_log.warn("Could not restore inventory: " + e.getMessage(), e);
		}
		return paperdoll;
	}

	public boolean validateCapacity(ItemInstance item) {
		int slots = 0;

		if (!(item.isStackable() && getItemByItemId(item.getItemId()) != null) && item.getItemType() != EtcItemType.HERB) {
			slots++;
		}

		return validateCapacity(slots);
	}

	public boolean validateCapacityByItemId(int ItemId) {
		int slots = 0;

		ItemInstance invItem = getItemByItemId(ItemId);
		if (!(invItem != null && invItem.isStackable())) {
			slots++;
		}

		return validateCapacity(slots);
	}

	@Override
	public boolean validateCapacity(int slots) {
		return (_items.size() + slots <= _owner.getInventoryLimit());
	}

	@Override
	public boolean validateWeight(int weight) {
		return (_totalWeight + weight <= _owner.getMaxLoad());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + _owner + "]";
	}

	@Override
	public int getPaperdollItemId(int slot) {
		if (_owner.getFirstEffect(L2EffectType.HEAVY_GRIP) != null) {
			final HeavyGrip effect = (HeavyGrip) _owner.getFirstEffect(L2EffectType.HEAVY_GRIP);
			switch (slot) {
				case Inventory.PAPERDOLL_RHAND:
					return effect.getWeaponItemId();
			}
		}
		return super.getPaperdollItemId(slot);
	}

	@Override
	public int getPaperdollObjectId(int slot) {
		if (_owner.getFirstEffect(L2EffectType.HEAVY_GRIP) != null) {
			final HeavyGrip effect = (HeavyGrip) _owner.getFirstEffect(L2EffectType.HEAVY_GRIP);
			switch (slot) {
				case Inventory.PAPERDOLL_RHAND:
					return effect.getWeaponObjectId();
			}
		}
		return super.getPaperdollObjectId(slot);
	}
	
	public void destroyAllItemsByType(ItemType type, String process, WorldObject reference) {
		final Collection<ItemInstance> items = cache.get(type);
		if(items == null || items.isEmpty()) {
			return;
		}
		
		final InventoryUpdate invenotryUpdate = new InventoryUpdate();
		for (ItemInstance item : items) {
			synchronized (item) {
				removeItem(item);
				World.getInstance().removeObject(item);
				IdFactory.getInstance().releaseId(item.getObjectId());

				// if it's a pet control item, delete the pet as well
				if (type == EtcItemType.PET_COLLAR) {
					try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?")) {
						statement.setInt(1, item.getObjectId());
						statement.execute();
					} catch (Exception e) {
						_log.warn("could not delete pet objectid:", e);
					}
				}				
				
				ItemDao.updateDatabase(item);
				invenotryUpdate.addItem(item);
			}
		}

		refreshWeight();
		
		// Send inventory update packet
		getOwner().sendPacket(invenotryUpdate);
		
		// Update current load as well
		final StatusUpdate su = new StatusUpdate(getOwner());
		su.addAttribute(StatusUpdate.CUR_LOAD, getOwner().getCurrentLoad());
		getOwner().sendPacket(su);
	}

}
