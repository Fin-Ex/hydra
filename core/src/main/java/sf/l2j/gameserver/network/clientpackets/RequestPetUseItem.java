package sf.l2j.gameserver.network.clientpackets;


import sf.l2j.commons.util.ArraysUtil;
import sf.l2j.gameserver.handler.HandlerTable;
import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.actor.Pet;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.PetItemList;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestPetUseItem extends L2GameClientPacket {

	private static final int[] PET_FOOD_IDS = {
		2515,
		4038,
		5168,
		5169,
		6316,
		7582
	};

	private int _objectId;

	@Override
	protected void readImpl() {
		_objectId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null || !activeChar.hasPet()) {
			return;
		}

		final Pet pet = (Pet) activeChar.getActiveSummon();

		final ItemInstance item = pet.getInventory().getItemByObjectId(_objectId);
		if (item == null) {
			return;
		}

		if (activeChar.isAlikeDead() || pet.isDead()) {
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(item));
			return;
		}

		if (!item.isEquipped() && !item.getItem().checkCondition(pet, pet, true)) {
			return;
		}

		// Check if item is pet armor or pet weapon
		if (item.isPetItem()) {
			// Verify if the pet can wear that item
			if (!pet.canWear(item.getItem())) {
				activeChar.sendPacket(SystemMessageId.PET_CANNOT_USE_ITEM);
				return;
			}

			if (item.isEquipped()) {
				pet.getInventory().unEquipItemInSlot(item.getLocationSlot());
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_TOOK_OFF_S1).addItemName(item));
			} else {
				pet.getInventory().equipPetItem(item);
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_PUT_ON_S1).addItemName(item));
			}

			activeChar.sendPacket(new PetItemList(pet));
			pet.updateAndBroadcastStatus(1);
			return;
		}

		if (ArraysUtil.contains(PET_FOOD_IDS, item.getItemId()) && !pet.getTemplate().canEatFood(item.getItemId())) {
			activeChar.sendPacket(SystemMessageId.PET_CANNOT_USE_ITEM);
			return;
		}

		// If pet food check is successful or if the item got an handler, use that item.
		final IHandler handler = HandlerTable.getInstance().get(item.getEtcItem().getHandlerName());
		if (handler != null) {
			handler.invoke(pet, item, false);
			pet.updateAndBroadcastStatus(1);
		} else {
			activeChar.sendPacket(SystemMessageId.PET_CANNOT_USE_ITEM);
		}
	}
}