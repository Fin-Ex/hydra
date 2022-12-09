package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.NewCharacterSuccessDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x0D))
public class NewCharacterSuccessSerializer implements PacketSerializer<NewCharacterSuccessDto> {

    @Override
    public void serialize(NewCharacterSuccessDto dto, ByteBuf buffer) {
        var prototypes = dto.getPrototypes();
        buffer.writeIntLE(prototypes.size());
        for (int i = 0; i < prototypes.size(); i++) {
            var prototype = prototypes.get(i);
            buffer.writeIntLE(prototype.getRace().getId());
            buffer.writeIntLE(prototype.getClassId());

            // m0nster.mind: idk what is 99 and 1, packet struct from l2j-unity
            buffer.writeIntLE(99);
            buffer.writeIntLE(prototype.getSTR());
            buffer.writeIntLE(1);

            buffer.writeIntLE(99);
            buffer.writeIntLE(prototype.getDEX());
            buffer.writeIntLE(1);

            buffer.writeIntLE(99);
            buffer.writeIntLE(prototype.getCON());
            buffer.writeIntLE(1);

            buffer.writeIntLE(99);
            buffer.writeIntLE(prototype.getINT());
            buffer.writeIntLE(1);

            buffer.writeIntLE(99);
            buffer.writeIntLE(prototype.getWIT());
            buffer.writeIntLE(1);

            buffer.writeIntLE(99);
            buffer.writeIntLE(prototype.getMEN());
            buffer.writeIntLE(1);
        }
    }

}
