package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.AuthLoginFailDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x0A))
public class AuthLoginFailSerializer implements PacketSerializer<AuthLoginFailDto> {

    @Override
    public void serialize(AuthLoginFailDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.isSuccess() ? -1 : 0x00);
        buffer.writeIntLE(dto.getMessageId());
    }

}
