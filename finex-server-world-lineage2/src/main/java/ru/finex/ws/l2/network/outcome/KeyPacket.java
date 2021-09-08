package ru.finex.ws.l2.network.outcome;

import lombok.Data;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x2e))
public final class KeyPacket extends L2GameServerPacket {

	private byte[] key;
	private int languageId = 1;
	private int serverId = 1;

	@Override
	public void writeImpl() {
		writeC(0x2e); // opcode
		writeC(0x01);
		writeB(key); // 7 bytes
		writeD(languageId);
		writeD(serverId);
		writeC(0x01);
		writeD(0x00); // obfuscation key
		writeC(0x00); // isClassic
	}
}
