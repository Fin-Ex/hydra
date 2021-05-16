package sf.l2j.gameserver.network.serverpackets;

import java.util.List;
import sf.finex.data.DyeData;
import sf.l2j.gameserver.model.actor.Player;

public class HennaEquipList extends L2GameServerPacket {

	private final Player player;
	private final List<DyeData> dyeEquipList;

	public HennaEquipList(Player player, List<DyeData> dyeEquipList) {
		this.player = player;
		this.dyeEquipList = dyeEquipList;
	}

	@Override
	protected final void writeImpl() {
		writeC(0xe2);
		writeD(player.getAdena());
		writeD(3);
		writeD(dyeEquipList.size());

		for (DyeData temp : dyeEquipList) {
			// Player must have at least one dye in inventory to be able to see the henna that can be applied with it.
			if ((player.getInventory().getItemByItemId(temp.getDyeId())) != null) {
				writeD(temp.getSymbolId()); // symbolid
				writeD(temp.getDyeId()); // itemid of dye
				writeD(DyeData.getRequiredDyeAmount()); // amount of dyes required
				writeD(temp.getPrice()); // amount of adenas required
				writeD(1); // meet the requirement or not
			}
		}
	}
}
