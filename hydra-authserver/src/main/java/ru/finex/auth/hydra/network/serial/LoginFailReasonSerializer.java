package ru.finex.auth.hydra.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.hydra.network.model.dto.LoginFailDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x01))
public class LoginFailReasonSerializer implements PacketSerializer<LoginFailDto> {

    @Override
    public void serialize(LoginFailDto dto, ByteBuf buffer) {
        buffer.writeByte(dto.getMessageId());
    }

}
