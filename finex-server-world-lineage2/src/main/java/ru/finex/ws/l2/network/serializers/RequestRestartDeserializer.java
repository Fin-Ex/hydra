package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.RestartCommand;
import ru.finex.ws.l2.network.model.dto.RequestRestartDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0x57)}, command = @Cmd(RestartCommand.class))
public class RequestRestartDeserializer implements PacketDeserializer<RequestRestartDto> {

    @Override
    public RequestRestartDto deserialize(ByteBuf buffer) {
        return RequestRestartDto.INSTANCE;
    }

}
