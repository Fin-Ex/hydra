package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.command.network.ValidatePositionCommand;
import ru.finex.ws.l2.network.model.dto.MoveToLocationDto;
import ru.finex.ws.l2.network.model.dto.ValidateLocationDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x2F))
public class MoveToLocationSerializer implements PacketSerializer<MoveToLocationDto> {

    @Override
    public void serialize(MoveToLocationDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getRuntimeId() + 1);
        buffer.writeIntLE(dto.getDestinationX());
        buffer.writeIntLE(dto.getDestinationY());
        buffer.writeIntLE(dto.getDestinationZ());
        buffer.writeIntLE(dto.getStartX());
        buffer.writeIntLE(dto.getStartY());
        buffer.writeIntLE(dto.getStartZ());
    }
}
