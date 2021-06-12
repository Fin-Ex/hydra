package ru.finex.gs.proto.interlude.outcome;

import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;

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
