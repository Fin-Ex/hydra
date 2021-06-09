package ru.finex.gs.network.outcome;

import ru.finex.gs.network.Opcode;
import ru.finex.gs.network.OutcomePacket;
import sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
@OutcomePacket(@Opcode(0x26))
public class ServerClose extends L2GameServerPacket {

    @Override
    protected void writeImpl() {
        writeC(0x26);
    }

}
