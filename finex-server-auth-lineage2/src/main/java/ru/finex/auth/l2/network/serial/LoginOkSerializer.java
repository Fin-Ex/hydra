package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.network.model.dto.LoginOkDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x03))
public class LoginOkSerializer implements PacketSerializer<LoginOkDto> {

    private static final byte[] BA_16 = new byte[16];

    @Override
    public void serialize(LoginOkDto dto, ByteBuf buffer) {
        buffer.writeLongLE(dto.getSessionKey());
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x000003ea);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeBytes(BA_16);
    }

}
