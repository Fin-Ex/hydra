package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.MoveToLocationDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x2F))
public class MoveToLocationSerializer implements PacketSerializer<MoveToLocationDto> {

    @Override
    public void serialize(MoveToLocationDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getRuntimeId());
        buffer.writeIntLE(dto.getDestinationX());
        buffer.writeIntLE(dto.getDestinationY());
        buffer.writeIntLE(dto.getDestinationZ());
        buffer.writeIntLE(dto.getPositionX());
        buffer.writeIntLE(dto.getPositionY());
        buffer.writeIntLE(dto.getPositionZ());
    }
}
