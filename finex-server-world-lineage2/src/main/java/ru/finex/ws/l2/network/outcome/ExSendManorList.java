package ru.finex.ws.l2.network.outcome;

import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author finfan
 */
@OutcomePacket({@Opcode(0xFE), @Opcode(0x22)})
public class ExSendManorList extends L2GameServerPacket {
	
	@Override
	protected void writeImpl() {
		writeC(0xfe);
		writeH(0x22);
		writeD(0); // count of castle
		// D castle id
	}
}
