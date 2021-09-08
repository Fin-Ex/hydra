package ru.finex.ws.l2.network.outcome;

import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author finfan
 */
@OutcomePacket({@Opcode(0xFE), @Opcode(0x1B)})
public class ExSendManorList extends L2GameServerPacket {
	
	@Override
	protected void writeImpl() {
		writeC(0xfe);
		writeH(0x1b);
		writeD(0); // count of castle
		// D castle id
		// S castle name
	}
}
