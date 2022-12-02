package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.ValidateLocationDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x79))
public class ValidateLocationSerializer implements PacketSerializer<ValidateLocationDto> {

    @Override
    public void serialize(ValidateLocationDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getVehicleId());
        buffer.writeIntLE(dto.getX());
        buffer.writeIntLE(dto.getY());
        buffer.writeIntLE(dto.getZ());
        buffer.writeIntLE(dto.getHeading());
        buffer.writeByte(0xFF); // TODO: Find me!
    }

}
