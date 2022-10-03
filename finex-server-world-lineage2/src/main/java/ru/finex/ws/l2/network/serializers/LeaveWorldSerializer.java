package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.LeaveWorldDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x84))
public class LeaveWorldSerializer implements PacketSerializer<LeaveWorldDto> {

    @Override
    public void serialize(LeaveWorldDto dto, ByteBuf buffer) {
        // empty packet
    }

}
