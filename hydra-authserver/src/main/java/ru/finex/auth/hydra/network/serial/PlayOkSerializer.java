package ru.finex.auth.hydra.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.hydra.network.model.dto.PlayOkDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x07))
public class PlayOkSerializer implements PacketSerializer<PlayOkDto> {

    @Override
    public void serialize(PlayOkDto dto, ByteBuf buffer) {
        buffer.writeLongLE(dto.getGameSessionKey());
    }

}
