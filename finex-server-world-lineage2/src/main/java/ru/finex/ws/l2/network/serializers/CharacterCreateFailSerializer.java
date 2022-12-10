package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.CharacterCreateFailDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x10))
public class CharacterCreateFailSerializer implements PacketSerializer<CharacterCreateFailDto> {

    @Override
    public void serialize(CharacterCreateFailDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getError().getId());
    }

}
