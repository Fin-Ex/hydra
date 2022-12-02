package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.model.enums.MoveType;
import ru.finex.ws.l2.network.model.dto.ChangeMoveTypeDto;
import ru.finex.ws.l2.network.model.dto.CharCreateFailDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x10))
public class CharCreateFailSerializer implements PacketSerializer<CharCreateFailDto> {

    @Override
    public void serialize(CharCreateFailDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getError().getId());
    }

}
