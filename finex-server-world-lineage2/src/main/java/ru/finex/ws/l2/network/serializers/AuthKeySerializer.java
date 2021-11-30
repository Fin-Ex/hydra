package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.network.IncomePacket;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.model.dto.AuthKeyDto;
import ru.finex.ws.l2.network.serial.PacketSerializer;
import ru.finex.ws.l2.network.serial.SerializerHelper;

/**
 * @author m0nster.mind
 */
@IncomePacket(@Opcode(0x2B))
public class AuthKeySerializer implements PacketSerializer<AuthKeyDto> {

    @Override
    public AuthKeyDto serialize(ByteBuf buffer) {
        return AuthKeyDto.builder()
            .login(SerializerHelper.readStringNullTerm(buffer).toLowerCase())
            .playKey1(buffer.readIntLE())
            .playKey2(buffer.readIntLE())
            .loginKey1(buffer.readIntLE())
            .loginKey2(buffer.readIntLE())
            .build();
    }

}
