package ru.finex.ws.l2.network.outcome;

import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
@OutcomePacket(@Opcode(0x20))
public class ServerClose extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeC(0x20);
    }

}
