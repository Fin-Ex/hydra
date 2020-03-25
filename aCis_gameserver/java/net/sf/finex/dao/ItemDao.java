/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.L2Augmentation;
import net.sf.l2j.gameserver.model.item.instance.EItemLocation;
import net.sf.l2j.gameserver.model.item.instance.type.HunterCardInstance;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;

/**
 *
 * @author finfan
 */
@Slf4j
public class ItemDao {

	private static final ReentrantLock locker = new ReentrantLock();

	public static final void insert(ItemInstance item) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement("INSERT INTO items (owner_id,item_id,count,loc,loc_data,enchant_level,object_id,custom_type1,custom_type2,mana_left,time) VALUES (?,?,?,?,?,?,?,?,?,?,?)")) {
			statement.setInt(1, item.getOwnerId());
			statement.setInt(2, item.getItemId());
			statement.setInt(3, item.getCount());
			statement.setString(4, item.getLocation().name());
			statement.setInt(5, item.getLocationData());
			statement.setInt(6, item.getEnchantLevel());
			statement.setInt(7, item.getObjectId());
			statement.setInt(8, item.getCustomType1());
			statement.setInt(9, item.getCustomType2());
			statement.setInt(10, item.getMana() * 60);
			statement.setLong(11, item.getTime());
			statement.executeUpdate();
			if (item.isAugmented()) {
				updateItemAttributes(item, con);
			}
			
			item.setStoredInDB(true);
			item.setExistInDB(true);
		} catch (Exception e) {
			log.error("Could not insert item {} into DB: Reason: {}", item, e.getMessage(), e);
		}
	}

	public static void updateItemAttributes(ItemInstance item, Connection pooledCon) {
		try (Connection con = pooledCon == null ? L2DatabaseFactory.getInstance().getConnection() : pooledCon; PreparedStatement statement = con.prepareStatement("REPLACE INTO augmentations VALUES(?,?,?,?)")) {
			statement.setInt(1, item.getObjectId());
			if (!item.isAugmented()) {
				statement.setInt(2, -1);
				statement.setInt(3, -1);
				statement.setInt(4, -1);
			} else {
				statement.setInt(2, item.getAugmentation().getAttributes());
				if (item.getAugmentation().getSkill() == null) {
					statement.setInt(3, 0);
					statement.setInt(4, 0);
				} else {
					statement.setInt(3, item.getAugmentation().getSkill().getId());
					statement.setInt(4, item.getAugmentation().getSkill().getLevel());
				}
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			log.error("Could not update attributes for item: {} from DB: ", item, e);
		}
	}

	public static void removeAugmentation(ItemInstance item) {
		item.setAugmentation(null);
		updateItemAttributes(item, null);
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement("DELETE FROM augmentations WHERE item_id = ?")) {
			statement.setInt(1, item.getObjectId());
			statement.executeUpdate();
		} catch (Exception e) {
			log.error("Could not remove augmentation for item: {} from DB: ", item, e);
		}
	}

	public static void restoreAttributes(ItemInstance item) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement("SELECT attributes,skill_id,skill_level FROM augmentations WHERE item_id=?")) {
			statement.setInt(1, item.getObjectId());
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					int aug_attributes = rs.getInt(1);
					int aug_skillId = rs.getInt(2);
					int aug_skillLevel = rs.getInt(3);
					if (aug_attributes != -1 && aug_skillId != -1 && aug_skillLevel != -1) {
						item.setAugmentation(new L2Augmentation(rs.getInt("attributes"), rs.getInt("skill_id"), rs.getInt("skill_level")));
						updateItemAttributes(item, null);
					}
				}
			}
		} catch (Exception e) {
			log.error("Could not restore augmentation data for item {} from DB: {}", item, e.getMessage(), e);
		}
	}

	/**
	 * @param ownerId : objectID of the owner.
	 * @param rs : the ResultSet of the item.
	 * @return a ItemInstance stored in database from its objectID
	 */
	public static ItemInstance restoreFromDb(int ownerId, ResultSet rs) {
		ItemInstance item = null;
		int objectId, item_id, loc_data, enchant_level, custom_type1, custom_type2, manaLeft, count;
		long time;
		EItemLocation loc;
		try {
			objectId = rs.getInt(1);
			item_id = rs.getInt("item_id");
			count = rs.getInt("count");
			loc = EItemLocation.valueOf(rs.getString("loc"));
			loc_data = rs.getInt("loc_data");
			enchant_level = rs.getInt("enchant_level");
			custom_type1 = rs.getInt("custom_type1");
			custom_type2 = rs.getInt("custom_type2");
			manaLeft = rs.getInt("mana_left");
			time = rs.getLong("time");
		} catch (SQLException e) {
			log.error("Could not restore an item owned by " + ownerId + " from DB:", e);
			return null;
		}

		final Item data = ItemTable.getInstance().getTemplate(item_id);
		if (data == null) {
			log.error("Item item_id={} not known, object_id={}", item_id, objectId);
			return null;
		}

		item = new ItemInstance(objectId, data);
		item.setOwnerId(ownerId);
		item.setCount(count);
		item.setEnchantLevel(enchant_level);
		item.setCustomType1(custom_type1);
		item.setCustomType2(custom_type2);
		item.setLocation(loc);
		item.setLocationData(loc_data);
		item.setStoredInDB(true);
		item.setExistInDB(true);

		// Setup life time for shadow weapons
		item.setMana(manaLeft);
		item.setTime(time);

		// load augmentation
		if (item.isEquipable()) {
			ItemDao.restoreAttributes(item);
		}

		return item;
	}

	/**
	 * Updates database.<BR>
	 * <BR>
	 * <U><I>Concept : </I></U><BR>
	 * <B>IF</B> the item exists in database :
	 * <UL>
	 * <LI><B>IF</B> the item has no owner, or has no location, or has a null
	 * quantity : remove item from database</LI>
	 * <LI><B>ELSE</B> : update item in database</LI>
	 * </UL>
	 * <B> Otherwise</B> :
	 * <UL>
	 * <LI><B>IF</B> the item hasn't a null quantity, and has a correct
	 * location, and has a correct owner : insert item in database</LI>
	 * </UL>
	 * @param item
	 */
	public static void updateDatabase(ItemInstance item) {
		locker.lock();

		try {
			if (item.isExistInDB()) {
				if (item.getOwnerId() == 0 || item.getLocation() == EItemLocation.VOID || (item.getCount() == 0 && item.getLocation() != EItemLocation.LEASE)) {
					removeFromDb(item);
				} else {
					updateInDb(item);
				}
			} else {
				if (item.getOwnerId() == 0 || item.getLocation() == EItemLocation.VOID || (item.getCount() == 0 && item.getLocation() != EItemLocation.LEASE)) {
					return;
				}

				insert(item);
			}
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Update the database with values of the item
	 */
	private static void updateInDb(ItemInstance item) {
		assert item.isExistInDB();

		if (item.isStoredInDB()) {
			return;
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement("UPDATE items SET owner_id=?,count=?,loc=?,loc_data=?,enchant_level=?,custom_type1=?,custom_type2=?,mana_left=?,time=? WHERE object_id = ?")) {
			statement.setInt(1, item.getOwnerId());
			statement.setInt(2, item.getCount());
			statement.setString(3, item.getLocation().name());
			statement.setInt(4, item.getLocationData());
			statement.setInt(5, item.getEnchantLevel());
			statement.setInt(6, item.getCustomType1());
			statement.setInt(7, item.getCustomType2());
			statement.setInt(8, item.getMana() * 60);
			statement.setLong(9, item.getTime());
			statement.setInt(10, item.getObjectId());
			statement.executeUpdate();
			item.setStoredInDB(true);
			item.setExistInDB(true);
		} catch (Exception e) {
			log.error("Could not update item {} in DB: Reason: {}", item, e.getMessage(), e);
		}
	}

	/**
	 * Delete item from database
	 */
	private static void removeFromDb(ItemInstance item) {
		assert item.isExistInDB();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement = con.prepareStatement("DELETE FROM items WHERE object_id=?");
			statement.setInt(1, item.getObjectId());
			statement.executeUpdate();
			item.setStoredInDB(false);
			item.setExistInDB(false);
			statement.close();

			if(item instanceof HunterCardInstance) {
				statement = con.prepareStatement("DELETE FROM items_hunter WHERE hunterId=?");
				statement.setInt(1, item.getOwnerId());
				statement.execute();
				statement.close();
			}
			
			statement = con.prepareStatement("DELETE FROM augmentations WHERE item_id = ?");
			statement.setInt(1, item.getObjectId());
			statement.executeUpdate();
			statement.close();
		} catch (Exception e) {
			log.error("Could not delete item {} in DB: {}", item, e.getMessage(), e);
		}
	}

}
