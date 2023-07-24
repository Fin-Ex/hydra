package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.hydra.network.model.dto.StopMoveDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x79))
public class StopMoveSerializer implements PacketSerializer<StopMoveDto> {

    @Override
    public void serialize(StopMoveDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getRuntimeId());
        buffer.writeIntLE(dto.getX());
        buffer.writeIntLE(dto.getY());
        buffer.writeIntLE(dto.getZ());
        buffer.writeIntLE(dto.getHeading());
    }

}
