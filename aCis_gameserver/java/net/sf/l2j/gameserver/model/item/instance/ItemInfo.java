package net.sf.l2j.gameserver.model.item.instance;

import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import lombok.Getter;
import net.sf.l2j.gameserver.model.item.instance.EItemState;
import net.sf.l2j.gameserver.model.item.kind.Item;

/**
 * Get all information from ItemInstance to generate ItemInfo.
 */
public class ItemInfo {

	@Getter
	private int objectId;
	@Getter
	private int itemId;
	@Getter
	private Item item;
	@Getter
	private int bodypart;
	@Getter
	private int enchant;
	@Getter
	private int augmentation;
	@Getter
	private int count;
	@Getter
	private int price;
	/**
	 * The custom ItemInstance types (used loto, race tickets)
	 */
	@Getter
	private int type1;
	@Getter
	private int type2;
	@Getter
	private int equipped;
	/**
	 * The action to do clientside (1=ADD, 2=MODIFY, 3=REMOVE)
	 */
	@Getter
	private EItemState change;
	@Getter
	private int durability;

	/**
	 * Get all information from ItemInstance to generate ItemInfo.
	 *
	 * @param item The item instance.
	 */
	public ItemInfo(ItemInstance item) {
		if (item == null) {
			return;
		}

		objectId = item.getObjectId();
		this.item = item.getItem();
		enchant = item.getEnchantLevel();
		augmentation = item.isAugmented() ? item.getAugmentation().getAugmentationId() : 0;
		count = item.getCount();
		// Get custom item types (used loto, race tickets)
		type1 = item.getCustomType1();
		type2 = item.getCustomType2();
		equipped = item.isEquipped() ? 1 : 0;
		// Get the action to do clientside
		change = item.getLastChange();
		durability = item.getMana();
	}

	public ItemInfo(ItemInstance item, EItemState change) {
		if (item == null) {
			return;
		}

		objectId = item.getObjectId();
		this.item = item.getItem();
		enchant = item.getEnchantLevel();
		augmentation = item.isAugmented() ? item.getAugmentation().getAugmentationId() : 0;
		count = item.getCount();
		// Get custom item types (used loto, race tickets)
		type1 = item.getCustomType1();
		type2 = item.getCustomType2();
		equipped = item.isEquipped() ? 1 : 0;
		this.change = change;
		durability = item.getMana();
	}
}
