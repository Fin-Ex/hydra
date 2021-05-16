package sf.l2j.gameserver.network.serverpackets;

import java.util.Set;

import sf.l2j.gameserver.model.actor.Pet;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.model.item.kind.Item;

public class PetItemList extends L2GameServerPacket {

	private final Set<ItemInstance> _items;

	public PetItemList(Pet character) {
		_items = character.getInventory().getItems();
	}

	@Override
	protected final void writeImpl() {
		writeC(0xB2);
		writeH(_items.size());

		for (ItemInstance temp : _items) {
			Item item = temp.getItem();

			writeH(item.getType1());
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeH(item.getType2());
			writeH(temp.getCustomType1());
			writeH(temp.isEquipped() ? 0x01 : 0x00);
			writeD(item.getBodyPart());
			writeH(temp.getEnchantLevel());
			writeH(temp.getCustomType2());
		}
	}
}
