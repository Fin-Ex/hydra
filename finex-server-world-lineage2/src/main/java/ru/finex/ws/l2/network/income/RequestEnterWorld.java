package ru.finex.ws.l2.network.income;

import lombok.extern.slf4j.Slf4j;
import ru.finex.ws.l2.command.network.EnterWorldCommand;
import ru.finex.ws.l2.network.IncomePacket;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Slf4j
@IncomePacket(value = @Opcode(0x11), command = EnterWorldCommand.class)
public class RequestEnterWorld extends L2GameClientPacket {

    @Override
    protected void readImpl() {
        for (int i = 0; i < 5; i++) {
            for (int o = 0; o < 4; o++) {
                readC();
            }
        }
        readD(); // Unknown Value
        readD(); // Unknown Value
        readD(); // Unknown Value
        readD(); // Unknown Value
        readB(64); // Unknown Byte Array
        readD(); // Unknown Value
    }

    @Override
    public <T extends NetworkDto> T getDto() {
        return null;
    }
}
