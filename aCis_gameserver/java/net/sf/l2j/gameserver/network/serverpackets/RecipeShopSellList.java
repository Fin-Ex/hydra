package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.ManufactureItemData;
import net.sf.l2j.gameserver.model.L2ManufactureList;
import net.sf.l2j.gameserver.model.actor.Player;

public class RecipeShopSellList extends L2GameServerPacket {

	private final Player _buyer, _manufacturer;

	public RecipeShopSellList(Player buyer, Player manufacturer) {
		_buyer = buyer;
		_manufacturer = manufacturer;
	}

	@Override
	protected final void writeImpl() {
		final L2ManufactureList createList = _manufacturer.getCreateList();
		if (createList != null) {
			writeC(0xd9);
			writeD(_manufacturer.getObjectId());
			writeD((int) _manufacturer.getCurrentMp());// Creator's MP
			writeD(_manufacturer.getMaxMp());// Creator's MP
			writeD(_buyer.getAdena());// Buyer Adena
			writeD(createList.size());

			for (ManufactureItemData item : createList.getList()) {
				writeD(item.getCraftId());
				writeD(0x00); // unknown
				writeD(item.getPrice());
			}
		}
	}
}
