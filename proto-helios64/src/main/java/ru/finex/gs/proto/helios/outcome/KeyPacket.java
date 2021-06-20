package ru.finex.gs.proto.helios.outcome;

import lombok.Data;
import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;

/**
 * @author finfan
 */
@Data
@OutcomePacket(@Opcode(0x2E))
public final class KeyPacket extends L2GameServerPacket {

	private byte[] key;
	private int result;
	private int languageId = 1;
	private int serverId = 1;

	@Override
	public void writeImpl() {
		writeC(0x2E);
		writeC(result); // 0 - wrong protocol, 1 - protocol ok
		for (int i = 0; i < 8; i++) {
			writeC(key[i]); // key
		}
		writeD(languageId);
		writeD(serverId);
		writeC(0x01);
		writeD(0x00); // obfuscation key
		writeC(0x00); // isClassic
	}
}
