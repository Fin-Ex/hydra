package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.network.model.dto.PlayFailDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x06))
public class PlayFailSerializer implements PacketSerializer<PlayFailDto> {

    @Override
    public void serialize(PlayFailDto dto, ByteBuf buffer) {
        buffer.writeByte(dto.getMessageId());
    }

}
