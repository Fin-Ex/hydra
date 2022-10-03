package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.model.dto.VersionCheckDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x2e))
public class VersionCheckSerializer implements PacketSerializer<VersionCheckDto> {

    @Override
    public void serialize(VersionCheckDto dto, ByteBuf buffer) {
        buffer.writeByte(dto.isValid() ? 0x01 : 0x00);
        buffer.writeBytes(dto.getKey(), 0, 8); // only dynamical part
        buffer.writeIntLE(dto.getLanguageId());
        buffer.writeIntLE(dto.getServerId());
        buffer.writeByte(0x01); // enable crypt
        buffer.writeIntLE(0x00); // obfuscation key
        buffer.writeByte(0x00); // server flags: 0x400 isClassic
    }

}
