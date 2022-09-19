package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.AuthClientCommand;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.AuthKeyDto;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = @Opcode(0x2B), command = @Cmd(AuthClientCommand.class))
public class AuthKeyDeserializer implements PacketDeserializer<AuthKeyDto> {

    @Override
    public AuthKeyDto deserialize(ByteBuf buffer) {
        return AuthKeyDto.builder()
            .login(SerializerHelper.readStringNullTerm(buffer).toLowerCase())
            .playKey1(buffer.readIntLE())
            .playKey2(buffer.readIntLE())
            .loginKey1(buffer.readIntLE())
            .loginKey2(buffer.readIntLE())
            .build();
    }

}
