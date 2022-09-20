package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.network.model.dto.GGAuthDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x0b))
public class GGAuthSerializer implements PacketSerializer<GGAuthDto> {

    @Override
    public void serialize(GGAuthDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getSessionId());
        buffer.writeIntLE(dto.getUnk1());
        buffer.writeIntLE(dto.getUnk2());
        buffer.writeIntLE(dto.getUnk3());
        buffer.writeIntLE(dto.getUnk4());
    }

}
