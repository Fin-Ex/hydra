package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.command.network.AuthGameGuardCommand;
import ru.finex.auth.l2.network.model.dto.AuthGameGuardDto;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = @Opcode(0x07), command = @Cmd(AuthGameGuardCommand.class))
public class AuthGameGuardDeserializer implements PacketDeserializer<AuthGameGuardDto> {

    @Override
    public AuthGameGuardDto deserialize(ByteBuf buffer) {
        return AuthGameGuardDto.builder()
            .sessionId(buffer.readIntLE())
            .unk1(buffer.readIntLE())
            .unk2(buffer.readIntLE())
            .unk3(buffer.readIntLE())
            .unk4(buffer.readIntLE())
            .build();
    }

}
