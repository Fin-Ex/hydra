package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.RestartResponseDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x71))
public class RestartResponseSerializer implements PacketSerializer<RestartResponseDto> {

    @Override
    public void serialize(RestartResponseDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getResult());
    }
}
