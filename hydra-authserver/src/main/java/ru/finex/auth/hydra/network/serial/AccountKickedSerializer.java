package ru.finex.auth.hydra.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.hydra.network.model.dto.AccountKickedDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x02))
public class AccountKickedSerializer implements PacketSerializer<AccountKickedDto> {

    @Override
    public void serialize(AccountKickedDto dto, ByteBuf buffer) {
        buffer.writeByte(dto.getMessageId());
    }

}
