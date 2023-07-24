package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.hydra.network.model.dto.IsCharacterNameCreatableDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket({@Opcode(0xFE), @Opcode(0x10b)})
public class IsCharacterNameCreatableSerializer implements PacketSerializer<IsCharacterNameCreatableDto> {

    @Override
    public void serialize(IsCharacterNameCreatableDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getMessageId());
    }

}
