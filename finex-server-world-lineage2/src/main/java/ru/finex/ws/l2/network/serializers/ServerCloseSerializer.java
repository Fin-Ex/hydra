package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.ServerCloseDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0xB0))
public class ServerCloseSerializer implements PacketSerializer<ServerCloseDto> {

    @Override
    public void serialize(ServerCloseDto dto, ByteBuf buffer) {

    }

}
