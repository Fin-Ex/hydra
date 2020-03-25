package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;

public final class RequestDestroyItem extends L2GameClientPacket {

	private int _objectId;
	private int _count;

	@Override
	protected void readImpl() {
		_objectId = readD();
		_count = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode()) {
			activeChar.sendPacket(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE);
			return;
		}

		final ItemInstance itemToRemove = activeChar.getInventory().getItemByObjectId(_objectId);
		if (itemToRemove == null) {
			return;
		}

		if (_count < 1 || _count > itemToRemove.getCount()) {
			activeChar.sendPacket(SystemMessageId.CANNOT_DESTROY_NUMBER_INCORRECT);
			return;
		}

		if (!itemToRemove.isStackable() && _count > 1) {
			return;
		}

		final int itemId = itemToRemove.getItemId();

		// Cannot discard item that the skill is consumming
		if (activeChar.isCastingNow()) {
			if (activeChar.getCurrentSkill().getSkill() != null && activeChar.getCurrentSkill().getSkill().getItemConsumeId() == itemId) {
				activeChar.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
				return;
			}
		}

		// Cannot discard item that the skill is consuming
		if (activeChar.isCastingSimultaneouslyNow()) {
			if (activeChar.getLastSimultaneousSkillCast() != null && activeChar.getLastSimultaneousSkillCast().getItemConsumeId() == itemId) {
				activeChar.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
				return;
			}
		}

		if (!itemToRemove.isDestroyable() || CursedWeaponsManager.getInstance().isCursed(itemId)) {
			if (itemToRemove.isHeroItem()) {
				activeChar.sendPacket(SystemMessageId.HERO_WEAPONS_CANT_DESTROYED);
			} else {
				activeChar.sendPacket(SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
			}
			return;
		}

		if (itemToRemove.isEquipped() && (!itemToRemove.isStackable() || (itemToRemove.isStackable() && _count >= itemToRemove.getCount()))) {
			ItemInstance[] unequipped = activeChar.getInventory().unEquipItemInSlotAndRecord(itemToRemove.getLocationSlot());
			InventoryUpdate iu = new InventoryUpdate();
			for (ItemInstance item : unequipped) {
				item.unChargeAllShots();
				iu.addModifiedItem(item);
			}

			activeChar.sendPacket(iu);
			activeChar.broadcastUserInfo();
		}

		// if it's a pet control item.
		if (itemToRemove.getItemType() == EtcItemType.PET_COLLAR) {
			// See if pet or mount is active ; can't destroy item linked to that pet.
			if ((activeChar.getActiveSummon() != null && activeChar.getActiveSummon().getControlItemId() == _objectId) || (activeChar.isMounted() && activeChar.getMountObjectId() == _objectId)) {
				activeChar.sendPacket(SystemMessageId.PET_SUMMONED_MAY_NOT_DESTROYED);
				return;
			}

			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?");
				statement.setInt(1, _objectId);
				statement.execute();
				statement.close();
			} catch (Exception e) {
				_log.warn("could not delete pet objectid: ", e);
			}
		}

		ItemInstance removedItem = activeChar.getInventory().destroyItem("Destroy", _objectId, _count, activeChar, null);
		if (removedItem == null) {
			return;
		}

		InventoryUpdate iu = new InventoryUpdate();
		if (removedItem.getCount() == 0) {
			iu.addRemovedItem(removedItem);
		} else {
			iu.addModifiedItem(removedItem);
		}

		activeChar.sendPacket(iu);

		StatusUpdate su = new StatusUpdate(activeChar);
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
	}
}
