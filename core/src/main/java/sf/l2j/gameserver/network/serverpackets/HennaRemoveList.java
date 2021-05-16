package sf.l2j.gameserver.network.serverpackets;

import sf.finex.model.dye.DyeComponent;
import sf.finex.data.DyeData;
import sf.l2j.gameserver.model.actor.Player;

public class HennaRemoveList extends L2GameServerPacket {

	private final DyeComponent dyeComponent;

	public HennaRemoveList(Player player) {
		dyeComponent = player.getComponent(DyeComponent.class);
	}

	@Override
	protected final void writeImpl() {
		writeC(0xe5);
		writeD(dyeComponent.getGameObject().getAdena());
		writeD(dyeComponent.getEmptySlots());
		writeD(Math.abs(dyeComponent.getEmptySlots() - 3));

		if (dyeComponent.getDyes() != null) {
			for (DyeData dye : dyeComponent.getDyes()) {
				if (dye == null) {
					continue;
				}

				writeD(dye.getSymbolId());
				writeD(dye.getDyeId());
				writeD(DyeData.getRequiredDyeAmount() / 2);
				writeD(dye.getPrice() / 5);
				writeD(0x01);
			}
		}
	}
}
