package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.ChangeMoveTypeDto;
import ru.finex.ws.l2.model.enums.MoveType;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x28))
public class ChangeMoveTypeSerializer implements PacketSerializer<ChangeMoveTypeDto> {

    @Override
    public void serialize(ChangeMoveTypeDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getRuntimeId());
        buffer.writeIntLE(MoveType.findBy(dto.isRunning()));
        buffer.writeIntLE(0x00); // ground type
    }

}
