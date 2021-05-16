package sf.l2j.gameserver.network.serverpackets;

import sf.finex.model.dye.DyeComponent;
import sf.finex.data.DyeData;
import sf.l2j.gameserver.model.actor.Player;

public class GMViewHennaInfo extends L2GameServerPacket {

	private final DyeComponent dyeComponent;
	private final DyeData[] dyes = new DyeData[3];
	private int count;

	public GMViewHennaInfo(Player activeChar) {
		dyeComponent = activeChar.getComponent(DyeComponent.class);
		count = 0;

		for (int i = 0; i < 3; i++) {
			final DyeData h = dyeComponent.getDye(i);
			if (h != null) {
				dyes[count++] = h;
			}
		}
	}

	@Override
	protected void writeImpl() {
		writeC(0xea);

		writeC(dyeComponent.getDyeINT());
		writeC(dyeComponent.getDyeSTR());
		writeC(dyeComponent.getDyeCON());
		writeC(dyeComponent.getDyeMEN());
		writeC(dyeComponent.getDyeDEX());
		writeC(dyeComponent.getDyeWIT());

		writeD(3); // slots?

		writeD(count); // size
		for (int i = 0; i < count; i++) {
			writeD(dyes[i].getSymbolId());
			writeD(dyes[i].getSymbolId()); // can be used by that class
		}
	}
}
