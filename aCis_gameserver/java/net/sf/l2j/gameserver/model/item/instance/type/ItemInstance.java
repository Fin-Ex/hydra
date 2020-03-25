package net.sf.l2j.gameserver.model.item.instance.type;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.Config;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.L2Augmentation;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.item.MercenaryTicket;
import net.sf.l2j.gameserver.model.item.instance.EItemLocation;
import net.sf.l2j.gameserver.model.item.instance.EItemState;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.item.type.ItemType;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.DropItem;
import net.sf.l2j.gameserver.network.serverpackets.GetItem;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SpawnItem;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.basefuncs.Func;
import net.sf.l2j.gameserver.taskmanager.ItemsOnGroundTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages items.
 */
@Slf4j
public class ItemInstance extends WorldObject implements Runnable, Comparable<ItemInstance> {
	protected static final Logger ITEM_LOG = LoggerFactory.getLogger("item");
	
	protected static final long REGULAR_LOOT_PROTECTION_TIME = 15000;
	protected static final long RAID_LOOT_PROTECTION_TIME = 300000;

	@Getter @Setter protected EItemState lastChange = EItemState.MODIFIED;
	@Getter protected int ownerId;
	@Getter @Setter protected int dropperObjectId;
	@Getter protected int count;
	@Getter @Setter protected int initCount;
	@Getter @Setter protected long time;
	@Getter @Setter protected boolean decrease;
	@Getter protected final int itemId;
	@Getter protected final Item item;
	@Getter @Setter protected boolean destroyProtected;
	private ScheduledFuture<?> dropProtection;
	@Getter protected EItemLocation location;
	@Getter @Setter protected int locationData;
	@Getter protected int enchantLevel;
	@Setter protected int mana = -1;
	@Getter @Setter protected int customType1;
	@Getter @Setter protected int customType2;
	@Getter @Setter private boolean isExistInDB; // if a record exists in DB.
	@Getter @Setter private boolean isStoredInDB; // if DB data is up-to-date.
	@Getter @Setter protected L2Augmentation augmentation;
	@Getter @Setter protected boolean isPickupable = true;
	@Getter @Setter protected boolean isAutodestroyable = true;
	private int shotMask;
	@Getter @Setter private Future<?> specialTask; // can be used for any task

	public ItemInstance(int objectId, int itemId) {
		super(objectId);
		this.itemId = itemId;
		item = ItemTable.getInstance().getTemplate(itemId);

		if (this.itemId == 0 || item == null) {
			throw new IllegalArgumentException();
		}

		super.setName(item.getName());
		setCount(1);
		location = EItemLocation.VOID;
		customType1 = 0;
		customType2 = 0;
		mana = item.getDuration() * 60;
	}

	public ItemInstance(int objectId, Item item) {
		super(objectId);
		itemId = item.getItemId();
		this.item = item;

		setName(this.item.getName());
		setCount(1);

		location = EItemLocation.VOID;
		mana = this.item.getDuration() * 60;
	}

	@Override
	public synchronized void run() {
		ownerId = 0;
		dropProtection = null;
	}

	public void restoreInitCount() {
		if (decrease) {
			count = initCount;
		}
	}

	public void actualizeTime() {
		time = System.currentTimeMillis();
	}

	public void setCount(int count) {
		if (getCount() == count) {
			return;
		}

		this.count = count >= -1 ? count : 0;
		isStoredInDB = false;
	}

	public void changeCount(String process, int count, Player creator, WorldObject reference) {
		if (count == 0) {
			return;
		}

		if (count > 0 && getCount() > Integer.MAX_VALUE - count) {
			setCount(Integer.MAX_VALUE);
		} else {
			setCount(getCount() + count);
		}

		if (getCount() < 0) {
			setCount(0);
		}

		isStoredInDB = false;

		if (Config.LOG_ITEMS && process != null) {
			ITEM_LOG.info("CHANGE: '{}'; Actor: {}, Item: {}, Reference: {}", process, creator, this, reference);
		}
	}

	public void setOwnerId(String process, int owner_id, Player creator, WorldObject reference) {
		setOwnerId(owner_id);

		if (Config.LOG_ITEMS) {
			ITEM_LOG.info("CHANGE: '{}'; Actor: {}, Item: {}, Reference: {}", process, creator, this, reference);
		}
	}

	public void setOwnerId(int owner_id) {
		if (owner_id == ownerId) {
			return;
		}

		ownerId = owner_id;
		isStoredInDB = false;
	}

	public synchronized boolean hasDropProtection() {
		return dropProtection != null;
	}

	public synchronized void setDropProtection(int ownerId, boolean isRaidParty) {
		this.ownerId = ownerId;
		dropProtection = ThreadPool.schedule(this, (isRaidParty) ? RAID_LOOT_PROTECTION_TIME : REGULAR_LOOT_PROTECTION_TIME);
	}

	public synchronized void removeDropProtection() {
		if (dropProtection != null) {
			dropProtection.cancel(true);
			dropProtection = null;
		}

		ownerId = 0;
	}

	public void setLocation(EItemLocation loc) {
		setLocation(loc, 0);
	}

	public void setLocation(EItemLocation loc, int loc_data) {
		if (loc == location && loc_data == locationData) {
			return;
		}

		location = loc;
		locationData = loc_data;
		isStoredInDB = false;
	}

	public boolean isOlyRestrictedItem() {
		return getItem().isOlyRestrictedItem();
	}

	public ItemType getItemType() {
		return item.getItemType();
	}

	@Override
	public void onAction(Player player) {
		if (player.isFlying()) {
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// Mercenaries tickets case.
		if (item.getItemType() == EtcItemType.CASTLE_GUARD) {
			if (player.isInParty()) {
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}

			final Castle castle = CastleManager.getInstance().getCastle(player);
			if (castle == null) {
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}

			final MercenaryTicket ticket = castle.getTicket(itemId);
			if (ticket == null) {
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}

			if (!player.isCastleLord(castle.getCastleId())) {
				player.sendPacket(SystemMessageId.THIS_IS_NOT_A_MERCENARY_OF_A_CASTLE_THAT_YOU_OWN_AND_SO_CANNOT_CANCEL_POSITIONING);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}

		player.getAI().setIntention(CtrlIntention.PICK_UP, this);
	}

	@Override
	public void onActionShift(Player player) {
		if (player.isGM()) {
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/admin/iteminfo.htm");
			html.replace("%objid%", getObjectId());
			html.replace("%itemid%", getItemId());
			html.replace("%ownerid%", getOwnerId());
			html.replace("%loc%", getLocation().toString());
			html.replace("%class%", getClass().getSimpleName());
			player.sendPacket(html);
		}
		super.onActionShift(player);
	}

	@Override
	public int compareTo(ItemInstance item) {
		final int tempTime = Long.compare(item.getTime(), this.time);
		if (tempTime != 0) {
			return tempTime;
		}

		return Integer.compare(item.getObjectId(), getObjectId());
	}

	public boolean isAugmented() {
		return augmentation != null;
	}

	public void setEnchantLevel(int enchantLevel) {
		if (this.enchantLevel == enchantLevel) {
			return;
		}

		this.enchantLevel = enchantLevel;
		isStoredInDB = false;
	}

	public boolean isShadowItem() {
		return mana >= 0;
	}

	public int decreaseMana(int period) {
		isStoredInDB = false;
		return mana -= period;
	}

	public int getMana() {
		return mana / 60;
	}
	
	@Override
	public void decayMe() {
		ItemsOnGroundTaskManager.getInstance().remove(this);
		super.decayMe();
	}

	@Override
	public void sendInfo(Player activeChar) {
		if (dropperObjectId != 0) {
			activeChar.sendPacket(new DropItem(this, dropperObjectId));
		} else {
			activeChar.sendPacket(new SpawnItem(this));
		}
	}

	public boolean isStackable() {
		return item.isStackable();
	}

	public boolean isEquipable() {
		return !(item.getBodyPart() == 0 || item.getItemType() == EtcItemType.ARROW || item.getItemType() == EtcItemType.LURE);
	}

	public boolean isEquipped() {
		return location == EItemLocation.PAPERDOLL || location == EItemLocation.PET_EQUIP;
	}

	public int getLocationSlot() {
		assert location == EItemLocation.PAPERDOLL || location == EItemLocation.PET_EQUIP || location == EItemLocation.FREIGHT;
		return locationData;
	}

	public boolean isEtc() {
		return (item instanceof EtcItem);
	}

	public boolean isWeapon() {
		return (item instanceof Weapon);
	}

	public boolean isArmor() {
		return (item instanceof Armor);
	}

	public EtcItem getEtcItem() {
		if (item instanceof EtcItem) {
			return (EtcItem) item;
		}

		return null;
	}

	public Weapon getWeaponItem() {
		if (item instanceof Weapon) {
			return (Weapon) item;
		}

		return null;
	}

	public Armor getArmorItem() {
		if (item instanceof Armor) {
			return (Armor) item;
		}

		return null;
	}


	/**
	 * Init a dropped ItemInstance and add it in the world as a visible
	 * object.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T ADD the
	 * object to _objects of World </B></FONT><BR>
	 * <BR>
	 *
	 * @param dropper : the character who dropped the item.
	 * @param x : X location of the item.
	 * @param y : Y location of the item.
	 * @param z : Z location of the item.
	 */
	public void dropMe(Creature dropper, int x, int y, int z) {
		ThreadPool.execute(() -> {
			int nX = x, nY = y, nZ = z;
			if (dropper != null) {
				final Location dropDest = GeoEngine.getInstance().canMoveToTargetLoc(dropper.getX(), dropper.getY(), dropper.getZ(), nX, nY, nZ);
				nX = dropDest.getX();
				nY = dropDest.getY();
				nZ = dropDest.getZ();
			}

			setDropperObjectId(dropper != null ? dropper.getObjectId() : 0); // Set the dropper Id for the knownlist packets in sendInfo
			spawnMe(nX, nY, nZ);
			setDropperObjectId(0);

			if (isAutodestroyable) {
				ItemsOnGroundTaskManager.getInstance().add(this, dropper);
			}
		});
	}
	
	public final void dropMe(Creature dropper, Location loc) {
		dropMe(dropper, loc.getX(), loc.getY(), loc.getZ());
	}

	public boolean isHerb() {
		return getItem().getItemType() == EtcItemType.HERB;
	}

	/**
	 * Remove a ItemInstance from the visible world and send server->client
	 * GetItem packets.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T REMOVE the
	 * object from _objects of World.</B></FONT><BR>
	 * <BR>
	 *
	 * @param pickUpper Player that pick up the item
	 */
	public void pickupMe(Creature pickUpper) {
		pickUpper.broadcastPacket(new GetItem(this, pickUpper.getPlayable()));

		// Unregister dropped ticket from castle, if that item is on a castle area and is a valid ticket.
		final Castle castle = CastleManager.getInstance().getCastle(pickUpper);
		if (castle != null && castle.getTicket(itemId) != null) {
			castle.removeDroppedTicket(this);
		}

		if (!Config.DISABLE_TUTORIAL && (itemId == 57 || itemId == 6353)) {
			Player actor = pickUpper.getPlayer();
			if (actor != null) {
				QuestState qs = actor.getQuestState("Tutorial");
				if (qs != null) {
					qs.getQuest().notifyEvent("CE" + itemId + "", null, actor);
				}
			}
		}

		// Calls directly setRegion(null), we don't have to care about.
		setIsVisible(false);
	}

	public final int getCrystalCount() {
		return item.getCrystalCount(enchantLevel);
	}

	public int getReferencePrice() {
		return item.getReferencePrice();
	}

	public String getItemName() {
		return item.getName();
	}

	public boolean isDropable() {
		return isAugmented() ? false : item.isDropable();
	}

	public boolean isDestroyable() {
		return isQuestItem() ? false : item.isDestroyable();
	}

	public boolean isTradable() {
		return isAugmented() ? false : item.isTradable();
	}

	public boolean isSellable() {
		return isAugmented() ? false : item.isSellable();
	}

	public boolean isDepositable(boolean isPrivateWareHouse) {
		// equipped, hero and quest items
		if (isEquipped() || !item.isDepositable()) {
			return false;
		}

		if (!isPrivateWareHouse) {
			// augmented not tradable
			if (!isTradable() || isShadowItem()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return if item is consumable.
	 */
	public boolean isConsumable() {
		return item.isConsumable();
	}

	/**
	 * @param player : the player to check.
	 * @param allowAdena : if true, count adenas.
	 * @param allowNonTradable : if true, count non tradable items.
	 * @return if item is available for manipulation.
	 */
	public boolean isAvailable(Player player, boolean allowAdena, boolean allowNonTradable) {
		return ((!isEquipped()) // Not equipped
				&& (getItem().getType2() != Item.TYPE2_QUEST) // Not Quest Item
				&& (getItem().getType2() != Item.TYPE2_MONEY || getItem().getType1() != Item.TYPE1_SHIELD_ARMOR) // not money, not shield
				&& (player.getActiveSummon() == null || getObjectId() != player.getActiveSummon().getControlItemId()) // Not Control item of currently summoned pet
				&& (player.getActiveEnchantItem() != this) // Not momentarily used enchant scroll
				&& (allowAdena || getItemId() != 57) // Not adena
				&& (player.getCurrentSkill().getSkill() == null || player.getCurrentSkill().getSkill().getItemConsumeId() != getItemId()) && (!player.isCastingSimultaneouslyNow() || player.getLastSimultaneousSkillCast() == null || player.getLastSimultaneousSkillCast().getItemConsumeId() != getItemId()) && (allowNonTradable || isTradable()));
	}

	@Override
	public boolean isAutoAttackable(Creature attacker) {
		return false;
	}

	public List<Func> getStatFuncs(Creature player) {
		return getItem().getStatFuncs(this, player);
	}

	@Override
	public String toString() {
		return "" + item;
	}

	public boolean isNightLure() {
		return ((itemId >= 8505 && itemId <= 8513) || itemId == 8485);
	}

	public boolean isPetItem() {
		return getItem().isPetItem();
	}

	public boolean isPotion() {
		return getItem().isPotion();
	}

	public boolean isElixir() {
		return getItem().isElixir();
	}

	public boolean isHeroItem() {
		return getItem().isHeroItem();
	}

	public boolean isQuestItem() {
		return getItem().isQuestItem();
	}

	public List<Quest> getQuestEvents() {
		return item.getQuestEvents();
	}

	@Override
	public boolean isChargedShot(ShotType type) {
		return (shotMask & type.getMask()) == type.getMask();
	}

	@Override
	public void setChargedShot(ShotType type, boolean charged) {
		if (charged) {
			shotMask |= type.getMask();
		} else {
			shotMask &= ~type.getMask();
		}
	}

	public void unChargeAllShots() {
		shotMask = 0;
	}
	
	public boolean isStorable() {
		return !(isEtc() && getItemType() == EtcItemType.GLT_ITEM);
	}

	@Override
	public Player getPlayer() {
		return ownerId > 0 ? World.getInstance().getPlayer(ownerId) : null;
	}
}
