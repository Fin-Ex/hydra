package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.AllFortressInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket({@Opcode(0xfe), @Opcode(0x15)})
public class AllFortressInfoDtoSerializer implements PacketSerializer<AllFortressInfoDto> {

    @Override
    public void serialize(AllFortressInfoDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getFortressIds().size());
        // [
        //  d - id
        //  s - owner name
        //  d - siege in progress
        //  d - owned time
        // ]
    }

}
