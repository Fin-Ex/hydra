package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.hydra.network.model.dto.CharacterCreateOk;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(value = {@Opcode(0x0F)})
public class CharacterCreateOkSerializer implements PacketSerializer<CharacterCreateOk> {

    @Override
    public void serialize(CharacterCreateOk dto, ByteBuf buffer) {
        buffer.writeIntLE(0x01);
    }
}
