package ru.finex.gs.network.income;

import lombok.extern.slf4j.Slf4j;
import ru.finex.gs.network.IncomePacket;
import ru.finex.gs.network.Opcode;
import sf.l2j.gameserver.network.clientpackets.L2GameClientPacket;

/**
 * @author m0nster.mind
 */
@Slf4j
@IncomePacket(@Opcode(0x03))
public class RequestEnterWorld extends L2GameClientPacket {

    @Override
    protected void readImpl() {

    }

    @Override
    protected void runImpl() {
        log.info("Player {} enter to the world", getClient());
    }
}
