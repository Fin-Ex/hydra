package ru.finex.ws.l2.network.income;

import lombok.Getter;
import ru.finex.ws.l2.command.network.AuthClientCommand;
import ru.finex.ws.l2.network.IncomePacket;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.dto.AuthKeyDto;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = @Opcode(0x08), command = AuthClientCommand.class)
public class AuthKey extends L2GameClientPacket {

    @Getter
    private AuthKeyDto dto;

    @Override
    protected void readImpl() {
        dto = AuthKeyDto.builder()
            .login(readS().toLowerCase())
            .playKey1(readD())
            .playKey2(readD())
            .loginKey1(readD())
            .loginKey2(readD())
            .build();
    }

}
