package net.sf.l2j.gameserver.model.actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import net.sf.finex.dao.ItemDao;
import net.sf.finex.data.TimeStamp;
import net.sf.finex.enums.EPartyLoot;
import net.sf.finex.model.regeneration.ERegenType;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.handler.itemhandlers.ItemSkills;
import net.sf.l2j.gameserver.handler.itemhandlers.PetFood;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.model.PetDataEntry;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.BabyPet;
import net.sf.l2j.gameserver.model.actor.stat.PetStat;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.actor.template.PetTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.item.type.ArmorType;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.itemcontainer.PetInventory;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PetInventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PetItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;
import net.sf.l2j.gameserver.taskmanager.ItemsOnGroundTaskManager;

public class Pet extends Summon {

	private int _curFed;
	private final PetInventory _inventory;
	private final int _controlItemId;
	private boolean _respawned;
	private final boolean _mountable;

	private Future<?> _feedTask;

	private PetDataEntry _petData;

	/**
	 * The Experience before the last Death Penalty
	 */
	private long _expBeforeDeath = 0;
	private int _curWeightPenalty = 0;

	private final Map<Integer, TimeStamp> _reuseTimeStamps = new ConcurrentHashMap<>();

	public Collection<TimeStamp> getReuseTimeStamps() {
		return _reuseTimeStamps.values();
	}

	public Map<Integer, TimeStamp> getReuseTimeStamp() {
		return _reuseTimeStamps;
	}

	public PetDataEntry getPetData() {
		return _petData;
	}

	public void setPetData(PetDataEntry value) {
		_petData = value;
	}

	/**
	 * Manage Feeding Task.
	 * <ul>
	 * <li>Feed or kill the pet depending on hunger level</li>
	 * <li>If pet has food in inventory and feed level drops below 55% then
	 * consume food from inventory</li>
	 * <li>Send a broadcastStatusUpdate packet for this L2PetInstance</li>
	 * </ul>
	 */
	class FeedTask implements Runnable {

		@Override
		public void run() {
			if (getPlayer() == null || getPlayer().getActiveSummon() == null || getPlayer().getActiveSummon().getObjectId() != getObjectId()) {
				stopFeed();
				return;
			}

			setCurrentFed((getCurrentFed() > getFeedConsume()) ? getCurrentFed() - getFeedConsume() : 0);

			ItemInstance food = getInventory().getItemByItemId(getTemplate().getFood1());
			if (food == null) {
				food = getInventory().getItemByItemId(getTemplate().getFood2());
			}

			if (food != null && checkAutoFeedState()) {
				final IHandler handler = HandlerTable.getInstance().get(PetFood.class);
				if (handler != null) {
					getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_TOOK_S1_BECAUSE_HE_WAS_HUNGRY).addItemName(food));
					handler.invoke(Pet.this, food, false);
				}
			} else if (getCurrentFed() == 0) {
				getPlayer().sendPacket(SystemMessageId.YOUR_PET_IS_VERY_HUNGRY);
				if (Rnd.get(100) < 30) {
					stopFeed();
					getPlayer().sendPacket(SystemMessageId.STARVING_GRUMPY_AND_FED_UP_YOUR_PET_HAS_LEFT);
					deleteMe(getPlayer());
					return;
				}
			} else if (getCurrentFed() < (0.10 * getPetData().getMaxMeal())) {
				getPlayer().sendPacket(SystemMessageId.YOUR_PET_IS_VERY_HUNGRY_PLEASE_BE_CAREFUL);
				if (Rnd.get(100) < 3) {
					stopFeed();
					getPlayer().sendPacket(SystemMessageId.STARVING_GRUMPY_AND_FED_UP_YOUR_PET_HAS_LEFT);
					deleteMe(getPlayer());
					return;
				}
			}

			if (checkHungryState()) {
				setWalking();
			} else {
				setRunning();
			}

			broadcastStatusUpdate();
		}

		private int getFeedConsume() {
			return (isAttackingNow()) ? getPetData().getMealInBattle() : getPetData().getMealInNormal();
		}
	}

	public Pet(int objectId, NpcTemplate template, Player owner, ItemInstance control) {
		super(objectId, template, owner);

		getPosition().set(owner.getX() + 50, owner.getY() + 100, owner.getZ());

		_controlItemId = control.getObjectId();

		_inventory = new PetInventory(this);

		_mountable = isMountable(template.getNpcId());
	}

	@Override
	public void initCharStat() {
		setStat(new PetStat(this));
	}

	@Override
	public PetStat getStat() {
		return (PetStat) super.getStat();
	}

	@Override
	public PetTemplate getTemplate() {
		return (PetTemplate) super.getTemplate();
	}

	public boolean isRespawned() {
		return _respawned;
	}

	@Override
	public int getSummonType() {
		return 2;
	}

	@Override
	public void onAction(Player player) {
		boolean isOwner = player.getObjectId() == getPlayer().getObjectId();
		if (isOwner && player != getPlayer()) {
			updateRefOwner(player);
		}

		super.onAction(player);
	}

	@Override
	public int getControlItemId() {
		return _controlItemId;
	}

	public ItemInstance getControlItem() {
		return getPlayer().getInventory().getItemByObjectId(_controlItemId);
	}

	public int getCurrentFed() {
		return _curFed;
	}

	public void setCurrentFed(int num) {
		_curFed = Math.min(num, getPetData().getMaxMeal());
	}

	/**
	 * Returns the pet's currently equipped weapon instance (if any).
	 */
	@Override
	public ItemInstance getActiveWeaponInstance() {
		return _inventory.getPaperdollItem(Inventory.PAPERDOLL_RHAND);
	}

	/**
	 * Returns the pet's currently equipped weapon (if any).
	 */
	@Override
	public Weapon getActiveWeaponItem() {
		ItemInstance weapon = getActiveWeaponInstance();
		if (weapon == null) {
			return null;
		}

		return (Weapon) weapon.getItem();
	}

	@Override
	public PetInventory getInventory() {
		return _inventory;
	}

	/**
	 * Destroys item from inventory and send a Server->Client InventoryUpdate
	 * packet to the Player.
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param objectId : int Item Instance identifier of the item to be
	 * destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @param sendMessage : boolean Specifies whether to send message to Client
	 * about this action
	 * @return boolean informing if the action was successfull
	 */
	@Override
	public boolean destroyItem(String process, int objectId, int count, WorldObject reference, boolean sendMessage) {
		ItemInstance item = _inventory.destroyItem(process, objectId, count, getPlayer(), reference);

		if (item == null) {
			if (sendMessage) {
				getPlayer().sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			}

			return false;
		}

		// Send Pet inventory update packet
		PetInventoryUpdate petIU = new PetInventoryUpdate();
		petIU.addItem(item);
		getPlayer().sendPacket(petIU);

		if (sendMessage) {
			if (count > 1) {
				getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED).addItemName(item.getItemId()).addItemNumber(count));
			} else {
				getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED).addItemName(item.getItemId()));
			}
		}
		return true;
	}

	/**
	 * Destroy item from inventory by using its <B>itemId</B> and send a
	 * Server->Client InventoryUpdate packet to the Player.
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @param sendMessage : boolean Specifies whether to send message to Client
	 * about this action
	 * @return boolean informing if the action was successfull
	 */
	@Override
	public boolean destroyItemByItemId(String process, int itemId, int count, WorldObject reference, boolean sendMessage) {
		ItemInstance item = _inventory.destroyItemByItemId(process, itemId, count, getPlayer(), reference);

		if (item == null) {
			if (sendMessage) {
				getPlayer().sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			}

			return false;
		}

		// Send Pet inventory update packet
		PetInventoryUpdate petIU = new PetInventoryUpdate();
		petIU.addItem(item);
		getPlayer().sendPacket(petIU);

		if (sendMessage) {
			if (count > 1) {
				getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED).addItemName(item.getItemId()).addItemNumber(count));
			} else {
				getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED).addItemName(item.getItemId()));
			}
		}
		return true;
	}

	@Override
	public void doPickupItem(WorldObject object) {
		if (isDead()) {
			return;
		}

		getAI().setIntention(CtrlIntention.IDLE);

		if (!(object instanceof ItemInstance)) {
			// dont try to pickup anything that is not an item :)
			_log.warn(getName() + " tried to pickup a wrong target: " + object);
			return;
		}

		broadcastPacket(new StopMove(getObjectId(), getX(), getY(), getZ(), getHeading()));
		ItemInstance usableItem = (ItemInstance) object;

		// Cursed weapons
		if (CursedWeaponsManager.getInstance().isCursed(usableItem.getItemId())) {
			getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1).addItemName(usableItem.getItemId()));
			return;
		}

		// Can't pickup shots and arrows.
		if (usableItem.getItem().getItemType() == EtcItemType.ARROW || usableItem.getItem().getItemType() == EtcItemType.SHOT) {
			getPlayer().sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return;
		}

		synchronized (usableItem) {
			if (!usableItem.isVisible()) {
				return;
			}

			if (!_inventory.validateCapacity(usableItem)) {
				getPlayer().sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_ANY_MORE_ITEMS);
				return;
			}

			if (!_inventory.validateWeight(usableItem, usableItem.getCount())) {
				getPlayer().sendPacket(SystemMessageId.UNABLE_TO_PLACE_ITEM_YOUR_PET_IS_TOO_ENCUMBERED);
				return;
			}

			if (usableItem.getOwnerId() != 0 && !getPlayer().isLooterOrInLooterParty(usableItem.getOwnerId())) {
				if (usableItem.getItemId() == 57) {
					getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1_ADENA).addNumber(usableItem.getCount()));
				} else if (usableItem.getCount() > 1) {
					getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S2_S1_S).addItemName(usableItem.getItemId()).addNumber(usableItem.getCount()));
				} else {
					getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1).addItemName(usableItem.getItemId()));
				}

				return;
			}

			if (usableItem.hasDropProtection()) {
				usableItem.removeDropProtection();
			}

			// If owner is in party and it isnt finders keepers, distribute the item instead of stealing it -.-
			final Party party = getPlayer().getParty();
			if (party != null && party.getLootRule() != EPartyLoot.ITEM_LOOTER) {
				party.distributeItem(getPlayer(), usableItem);
			} else {
				usableItem.pickupMe(this);
			}

			// Item must be removed from ItemsOnGroundManager if it is active.
			ItemsOnGroundTaskManager.getInstance().remove(usableItem);
		}

		// Auto use herbs - pick up
		if (usableItem.getItemType() == EtcItemType.HERB) {
				final IHandler handler = HandlerTable.getInstance().get(ItemSkills.class);
			if (handler != null) {
				handler.invoke(this, usableItem, false);
			}

			ItemTable.getInstance().destroyItem("Consume", usableItem, getPlayer(), null);
			broadcastStatusUpdate();
		} else {
			// if item is instance of L2ArmorType or WeaponType broadcast an "Attention" system message
			if (usableItem.getItemType() instanceof ArmorType || usableItem.getItemType() instanceof WeaponType) {
				SystemMessage msg;
				if (usableItem.getEnchantLevel() > 0) {
					msg = SystemMessage.getSystemMessage(SystemMessageId.ATTENTION_S1_PET_PICKED_UP_S2_S3).addCharName(getPlayer()).addNumber(usableItem.getEnchantLevel()).addItemName(usableItem.getItemId());
				} else {
					msg = SystemMessage.getSystemMessage(SystemMessageId.ATTENTION_S1_PET_PICKED_UP_S2).addCharName(getPlayer()).addItemName(usableItem.getItemId());
				}

				getPlayer().broadcastPacket(msg, 1400);
			}

			SystemMessage sm2;
			if (usableItem.getItemId() == 57) {
				sm2 = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S1_ADENA).addItemNumber(usableItem.getCount());
			} else if (usableItem.getEnchantLevel() > 0) {
				sm2 = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S1_S2).addNumber(usableItem.getEnchantLevel()).addItemName(usableItem.getItemId());
			} else if (usableItem.getCount() > 1) {
				sm2 = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S2_S1_S).addItemName(usableItem.getItemId()).addItemNumber(usableItem.getCount());
			} else {
				sm2 = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S1).addItemName(usableItem.getItemId());
			}

			getPlayer().sendPacket(sm2);
			getInventory().addItem("Pickup", usableItem, getPlayer(), this);
			getPlayer().sendPacket(new PetItemList(this));
		}

		if (getFollowStatus()) {
			followOwner();
		}
	}

	@Override
	public void deleteMe(Player owner) {
		getInventory().deleteMe();
		super.deleteMe(owner);
		destroyControlItem(owner); // this should also delete the pet from the db
	}

	@Override
	public boolean doDie(Creature killer) {
		if (!super.doDie(killer)) {
			return false;
		}

		stopFeed();
		getPlayer().sendPacket(SystemMessageId.MAKE_SURE_YOU_RESSURECT_YOUR_PET_WITHIN_20_MINUTES);
		DecayTaskManager.getInstance().add(this, 1200);

		// Dont decrease exp if killed in duel or arena
		Player owner = getPlayer();
		if (owner != null && !owner.isInDuel() && (!isInsideZone(ZoneId.PVP) || isInsideZone(ZoneId.SIEGE))) {
			deathPenalty();
		}

		return true;
	}

	@Override
	public void doRevive() {
		getPlayer().setReviveRequest(null);

		super.doRevive();

		// stopDecay
		DecayTaskManager.getInstance().cancel(this);
		startFeed();

		if (!checkHungryState()) {
			setRunning();
		}

		getAI().setIntention(CtrlIntention.ACTIVE, null);
	}

	@Override
	public void doRevive(double revivePower) {
		// Restore the pet's lost experience depending on the % return of the skill used
		restoreExp(revivePower);
		doRevive();
	}

	/**
	 * Transfers item to another inventory
	 *
	 * @param process : String Identifier of process triggering this action
	 * @param objectId : ObjectId of the item to be transfered
	 * @param count : int Quantity of items to be transfered
	 * @param target : The Inventory to target
	 * @param actor : Player Player requesting the item transfer
	 * @param reference : WorldObject Object referencing current action like NPC
	 * selling item or previous item in transformation
	 * @return ItemInstance corresponding to the new item or the updated item in
	 * inventory
	 */
	public ItemInstance transferItem(String process, int objectId, int count, Inventory target, Player actor, WorldObject reference) {
		final ItemInstance oldItem = checkItemManipulation(objectId, count);
		if (oldItem == null) {
			return null;
		}

		final boolean wasWorn = oldItem.isPetItem() && oldItem.isEquipped();

		final ItemInstance newItem = getInventory().transferItem(process, objectId, count, target, actor, reference);
		if (newItem == null) {
			return null;
		}

		// Send pet inventory update packet
		PetInventoryUpdate petIU = new PetInventoryUpdate();
		if (oldItem.getCount() > 0 && oldItem != newItem) {
			petIU.addModifiedItem(oldItem);
		} else {
			petIU.addRemovedItem(oldItem);
		}
		sendPacket(petIU);

		// Send player inventory update packet
		InventoryUpdate playerIU = new InventoryUpdate();
		if (newItem.getCount() > count) {
			playerIU.addModifiedItem(newItem);
		} else {
			playerIU.addNewItem(newItem);
		}
		sendPacket(playerIU);

		// Update player current load aswell
		StatusUpdate playerSU = new StatusUpdate(getPlayer());
		playerSU.addAttribute(StatusUpdate.CUR_LOAD, getPlayer().getCurrentLoad());
		sendPacket(playerSU);

		if (wasWorn) {
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_TOOK_OFF_S1).addItemName(newItem));
		}

		return newItem;
	}

	public ItemInstance checkItemManipulation(int objectId, int count) {
		final ItemInstance item = getInventory().getItemByObjectId(objectId);
		if (item == null) {
			return null;
		}

		if (count < 1 || (count > 1 && !item.isStackable())) {
			return null;
		}

		if (count > item.getCount()) {
			return null;
		}

		return item;
	}

	/**
	 * Remove the Pet from DB and its associated item from the player inventory
	 *
	 * @param owner The owner from whose invenory we should delete the item
	 */
	public void destroyControlItem(Player owner) {
		// remove the pet instance from world
		World.getInstance().removePet(owner.getObjectId());

		// delete from inventory
		try {
			ItemInstance removedItem = owner.getInventory().destroyItem("PetDestroy", getControlItemId(), 1, getPlayer(), this);

			if (removedItem == null) {
				_log.warn("Couldn't destroy petControlItem for " + owner.getName() + ", pet: " + this);
			} else {
				InventoryUpdate iu = new InventoryUpdate();
				iu.addRemovedItem(removedItem);

				owner.sendPacket(iu);

				StatusUpdate su = new StatusUpdate(owner);
				su.addAttribute(StatusUpdate.CUR_LOAD, owner.getCurrentLoad());
				owner.sendPacket(su);

				owner.broadcastUserInfo();

				World.getInstance().removeObject(removedItem);
			}
		} catch (Exception e) {
			_log.warn("Error while destroying control item: " + e.getMessage(), e);
		}

		// pet control item no longer exists, delete the pet from the db
		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?");
			statement.setInt(1, getControlItemId());
			statement.execute();
			statement.close();
		} catch (Exception e) {
			_log.error("Failed to delete Pet [ObjectId: " + getObjectId() + "]", e);
		}
	}

	/**
	 * @return Returns the mountable.
	 */
	@Override
	public boolean isMountable() {
		return _mountable;
	}

	public static Pet restore(ItemInstance control, NpcTemplate template, Player owner) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			Pet pet;
			if (template.isType("BabyPet")) {
				pet = new BabyPet(IdFactory.getInstance().getNextId(), template, owner, control);
			} else {
				pet = new Pet(IdFactory.getInstance().getNextId(), template, owner, control);
			}

			PreparedStatement statement = con.prepareStatement("SELECT item_obj_id, name, level, curHp, curMp, exp, sp, fed FROM pets WHERE item_obj_id=?");
			statement.setInt(1, control.getObjectId());
			ResultSet rset = statement.executeQuery();
			if (!rset.next()) {
				rset.close();
				statement.close();

				pet.getStat().setLevel((template.getNpcId() == 12564) ? (byte) pet.getPlayer().getLevel() : template.getLevel());
				pet.getStat().setExp(pet.getExpForThisLevel());
				pet.getStatus().setCurrentHp(pet.getMaxHp());
				pet.getStatus().setCurrentMp(pet.getMaxMp());
				pet.setCurrentFed(pet.getPetData().getMaxMeal());
				pet.store();

				return pet;
			}

			pet._respawned = true;
			pet.setName(rset.getString("name"));

			pet.getStat().setLevel(rset.getByte("level"));
			pet.getStat().setExp(rset.getLong("exp"));
			pet.getStat().setSp(rset.getInt("sp"));

			pet.getStatus().setCurrentHp(rset.getDouble("curHp"));
			pet.getStatus().setCurrentMp(rset.getDouble("curMp"));
			if (rset.getDouble("curHp") < 0.5) {
				pet.setIsDead(true);
				pet.getStatus().stopRegen(ERegenType.VALUES);
			}

			pet.setCurrentFed(rset.getInt("fed"));

			rset.close();
			statement.close();
			return pet;
		} catch (Exception e) {
			_log.warn("Could not restore pet data for owner: " + owner + " - " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void store() {
		if (getControlItemId() == 0) {
			return;
		}

		String req;
		if (!isRespawned()) {
			req = "INSERT INTO pets (name,level,curHp,curMp,exp,sp,fed,item_obj_id) VALUES (?,?,?,?,?,?,?,?)";
		} else {
			req = "UPDATE pets SET name=?,level=?,curHp=?,curMp=?,exp=?,sp=?,fed=? WHERE item_obj_id = ?";
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement = con.prepareStatement(req);
			statement.setString(1, getName());
			statement.setInt(2, getStat().getLevel());
			statement.setDouble(3, getStatus().getCurrentHp());
			statement.setDouble(4, getStatus().getCurrentMp());
			statement.setLong(5, getStat().getExp());
			statement.setInt(6, getStat().getSp());
			statement.setInt(7, getCurrentFed());
			statement.setInt(8, getControlItemId());
			statement.executeUpdate();
			statement.close();
			_respawned = true;
		} catch (Exception e) {
			_log.error("Failed to store Pet [ObjectId: " + getObjectId() + "] data", e);
		}

		ItemInstance itemInst = getControlItem();
		if (itemInst != null && itemInst.getEnchantLevel() != getStat().getLevel()) {
			itemInst.setEnchantLevel(getStat().getLevel());
			ItemDao.updateDatabase(itemInst);
		}
	}

	public synchronized void stopFeed() {
		if (_feedTask != null) {
			_feedTask.cancel(false);
			_feedTask = null;
		}
	}

	public synchronized void startFeed() {
		// stop feeding task if its active
		stopFeed();

		if (!isDead() && getPlayer().getActiveSummon() == this) {
			_feedTask = ThreadPool.scheduleAtFixedRate(new FeedTask(), 10000, 10000);
		}
	}

	@Override
	public synchronized void unSummon(Player owner) {
		// First, stop feed task.
		stopFeed();

		// Then drop inventory.
		if (!isDead()) {
			if (getInventory() != null) {
				getInventory().deleteMe();
			}
		}

		// Finally drop pet itself.
		super.unSummon(owner);

		// Drop pet from world's pet list.
		if (!isDead()) {
			World.getInstance().removePet(owner.getObjectId());
		}
	}

	/**
	 * Restore the specified % of experience this L2PetInstance has lost.
	 *
	 * @param restorePercent
	 */
	public void restoreExp(double restorePercent) {
		if (_expBeforeDeath > 0) {
			// Restore the specified % of lost experience.
			getStat().addExp(Math.round((_expBeforeDeath - getStat().getExp()) * restorePercent / 100));
			_expBeforeDeath = 0;
		}
	}

	private void deathPenalty() {
		int lvl = getStat().getLevel();
		double percentLost = -0.07 * lvl + 6.5;

		// Calculate the Experience loss
		long lostExp = Math.round((getStat().getExpForLevel(lvl + 1) - getStat().getExpForLevel(lvl)) * percentLost / 100);

		// Get the Experience before applying penalty
		_expBeforeDeath = getStat().getExp();

		// Set the new Experience value of the L2PetInstance
		getStat().addExp(-lostExp);
	}

	@Override
	public void addExpAndSp(long addToExp, int addToSp) {
		getStat().addExpAndSp(Math.round(addToExp * ((getNpcId() == 12564) ? Config.SINEATER_XP_RATE : Config.PET_XP_RATE)), addToSp);
	}

	@Override
	public long getExpForThisLevel() {
		return getStat().getExpForLevel(getLevel());
	}

	@Override
	public long getExpForNextLevel() {
		return getStat().getExpForLevel(getLevel() + 1);
	}

	@Override
	public final int getLevel() {
		return getStat().getLevel();
	}

	@Override
	public final int getSkillLevel(int skillId) {
		// Unknown skill. Return -1.
		if (getSkill(skillId) == null) {
			return -1;
		}

		// Max level for pet is 80, max level for pet skills is 12 => ((80 - 8) / 6) = 12.
		return Math.max(1, Math.min((getLevel() - 8) / 6, SkillTable.getInstance().getMaxLevel(skillId)));
	}

	public void updateRefOwner(Player owner) {
		int oldOwnerId = getPlayer().getObjectId();

		setOwner(owner);
		World.getInstance().removePet(oldOwnerId);
		World.getInstance().addPet(oldOwnerId, this);
	}

	public int getCurrentLoad() {
		return _inventory.getTotalWeight();
	}

	@Override
	public final int getMaxLoad() {
		return PetTemplate.MAX_LOAD;
	}

	@Override
	public int getSoulShotsPerHit() {
		return getPetData().getSsCount();
	}

	@Override
	public int getSpiritShotsPerHit() {
		return getPetData().getSpsCount();
	}

	public int getInventoryLimit() {
		return Config.INVENTORY_MAXIMUM_PET;
	}

	public void refreshOverloaded() {
		int maxLoad = getMaxLoad();
		if (maxLoad > 0) {
			int weightproc = getCurrentLoad() * 1000 / maxLoad;
			int newWeightPenalty;

			if (weightproc < 500) {
				newWeightPenalty = 0;
			} else if (weightproc < 666) {
				newWeightPenalty = 1;
			} else if (weightproc < 800) {
				newWeightPenalty = 2;
			} else if (weightproc < 1000) {
				newWeightPenalty = 3;
			} else {
				newWeightPenalty = 4;
			}

			if (_curWeightPenalty != newWeightPenalty) {
				_curWeightPenalty = newWeightPenalty;
				if (newWeightPenalty > 0) {
					addSkill(SkillTable.getInstance().getInfo(4270, newWeightPenalty));
					setIsOverloaded(getCurrentLoad() >= maxLoad);
				} else {
					super.removeSkill(getSkill(4270));
					setIsOverloaded(false);
				}
			}
		}
	}

	@Override
	public void updateAndBroadcastStatus(int val) {
		refreshOverloaded();
		super.updateAndBroadcastStatus(val);
	}

	/**
	 * @return true if the auto feed limit is reached, false otherwise or if
	 * there is no need to feed.
	 */
	public boolean checkAutoFeedState() {
		return getCurrentFed() < (_petData.getMaxMeal() * getTemplate().getAutoFeedLimit());
	}

	/**
	 * @return true if the hungry limit is reached, false otherwise or if there
	 * is no need to feed.
	 */
	public boolean checkHungryState() {
		return getCurrentFed() < (_petData.getMaxMeal() * getTemplate().getHungryLimit());
	}

	/**
	 * @return true if the unsummon limit is reached, false otherwise or if
	 * there is no need to feed.
	 */
	public boolean checkUnsummonState() {
		return getCurrentFed() < (_petData.getMaxMeal() * getTemplate().getUnsummonLimit());
	}

	public boolean canWear(Item item) {
		final int npcId = getTemplate().getNpcId();

		if (npcId > 12310 && npcId < 12314 && item.getBodyPart() == Item.SLOT_HATCHLING) {
			return true;
		}

		if (npcId == 12077 && item.getBodyPart() == Item.SLOT_WOLF) {
			return true;
		}

		if (npcId > 12525 && npcId < 12529 && item.getBodyPart() == Item.SLOT_STRIDER) {
			return true;
		}

		if (npcId > 12779 && npcId < 12783 && item.getBodyPart() == Item.SLOT_BABYPET) {
			return true;
		}

		return false;
	}

	@Override
	public final int getWeapon() {
		ItemInstance weapon = getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if (weapon != null) {
			return weapon.getItemId();
		}

		return 0;
	}

	@Override
	public final int getArmor() {
		ItemInstance weapon = getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		if (weapon != null) {
			return weapon.getItemId();
		}

		return 0;
	}

	@Override
	public void setName(String name) {
		ItemInstance controlItem = getControlItem();
		if (controlItem.getCustomType2() == (name == null ? 1 : 0)) {
			// Name isn't setted yet.
			controlItem.setCustomType2(name != null ? 1 : 0);
			ItemDao.updateDatabase(controlItem);

			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(controlItem);
			getPlayer().sendPacket(iu);
		}
		super.setName(name);
	}

	/**
	 * Index according to skill id the current timestamp of use.
	 *
	 * @param skill
	 * @param reuse delay
	 */
	@Override
	public void addTimeStamp(L2Skill skill, long reuse) {
		final TimeStamp ts = new TimeStamp();
		ts.setSkillId(skill.getId());
		ts.setSkillLvl(skill.getLevel());
		_reuseTimeStamps.put(skill.getReuseHashCode(), ts);
	}

	private static boolean isMountable(int npcId) {
		return npcId == 12526 // wind strider
				|| npcId == 12527 // star strider
				|| npcId == 12528 // twilight strider
				|| npcId == 12621; // wyvern
	}

	@Override
	public boolean isPet() {
		return true;
	}

	@Override
	public Pet getPet() {
		return this;
	}
}
