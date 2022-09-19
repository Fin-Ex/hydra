package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.ManorListDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket({@Opcode(0xFE), @Opcode(0x22)})
public class ExSendManorListSerializer implements PacketSerializer<ManorListDto> {

    @Override
    public void serialize(ManorListDto dto, ByteBuf buffer) {
        var castles = dto.getCastleIds();
        buffer.writeIntLE(castles.size());
        for (Integer castleId : castles) {
            buffer.writeIntLE(castleId);
        }
    }

}
