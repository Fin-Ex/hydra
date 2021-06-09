package ru.finex.gs.network.outcome;

import lombok.Data;
import ru.finex.gs.network.Opcode;
import ru.finex.gs.network.OutcomePacket;
import sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x00))
public final class KeyPacket extends L2GameServerPacket {

	private byte[] key;
	private int languageId = 1;
	private int serverId = 1;

	@Override
	public void writeImpl() {
		writeC(0x00); // opcode
		writeC(0x01);
		writeB(key); // 7 bytes
		writeD(languageId);
		writeD(serverId);
	}
}
