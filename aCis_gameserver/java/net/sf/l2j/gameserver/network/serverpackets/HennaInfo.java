package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.model.dye.DyeComponent;
import net.sf.finex.data.DyeData;
import net.sf.l2j.gameserver.model.actor.Player;

public final class HennaInfo extends L2GameServerPacket {

	private final DyeComponent dyeComponent;
	private final DyeData[] dyes = new DyeData[3];
	private int count;

	public HennaInfo(Player player) {
		dyeComponent = player.getComponent(DyeComponent.class);
		count = 0;

		for (int i = 0; i < 3; i++) {
			DyeData henna = dyeComponent.getDye(i);
			if (henna != null) {
				dyes[count++] = henna;
			}
		}
	}

	@Override
	protected final void writeImpl() {
		writeC(0xe4);

		writeC(dyeComponent.getDyeINT()); // equip INT
		writeC(dyeComponent.getDyeSTR()); // equip STR
		writeC(dyeComponent.getDyeCON()); // equip CON
		writeC(dyeComponent.getDyeMEN()); // equip MEM
		writeC(dyeComponent.getDyeDEX()); // equip DEX
		writeC(dyeComponent.getDyeWIT()); // equip WIT
		writeD(3); // henna slots opened by class level
		writeD(count); // size
		for (int i = 0; i < count; i++) {
			writeD(dyes[i].getSymbolId());
			writeD(dyes[i].getSymbolId()); // can be used by that class
		}
	}
}
