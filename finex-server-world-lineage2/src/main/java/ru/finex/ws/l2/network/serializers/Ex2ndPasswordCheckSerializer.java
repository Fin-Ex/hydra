package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.Ex2ndPasswordCheckDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket({@Opcode(0xfe), @Opcode(0x105)})
public class Ex2ndPasswordCheckSerializer implements PacketSerializer<Ex2ndPasswordCheckDto> {

    @Override
    public void serialize(Ex2ndPasswordCheckDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getReason());
        buffer.writeIntLE(0x00);
    }

}
