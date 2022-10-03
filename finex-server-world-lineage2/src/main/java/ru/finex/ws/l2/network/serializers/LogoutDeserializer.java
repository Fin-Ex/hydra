package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.LogoutCommand;
import ru.finex.ws.l2.network.model.dto.LogoutDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x00), command = @Cmd(LogoutCommand.class))
public class LogoutDeserializer implements PacketDeserializer<LogoutDto> {

    @Override
    public LogoutDto deserialize(ByteBuf buffer) {
        return LogoutDto.INSTANCE;
    }

}
