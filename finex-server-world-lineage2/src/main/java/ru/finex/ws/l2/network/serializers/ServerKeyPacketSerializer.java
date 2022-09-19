package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.ServerKeyDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x2e))
public class ServerKeyPacketSerializer implements PacketSerializer<ServerKeyDto> {

    @Override
    public void serialize(ServerKeyDto dto, ByteBuf buffer) {
        buffer.writeByte(0x01);
        buffer.writeBytes(dto.getKey()); // 7 bytes
        buffer.writeIntLE(dto.getLanguageId());
        buffer.writeIntLE(dto.getServerId());
        buffer.writeByte(0x01);
        buffer.writeIntLE(0x00); // obfuscation key
        buffer.writeByte(0x00); // isClassic
    }

}
