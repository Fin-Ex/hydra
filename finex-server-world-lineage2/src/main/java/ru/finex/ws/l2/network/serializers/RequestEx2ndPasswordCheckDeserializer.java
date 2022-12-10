package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.Ex2ndPasswordCheckCommand;
import ru.finex.ws.l2.network.model.dto.RequestEx2ndPasswordCheckDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0xa6)}, command = @Cmd(Ex2ndPasswordCheckCommand.class))
public class RequestEx2ndPasswordCheckDeserializer implements PacketDeserializer<RequestEx2ndPasswordCheckDto> {

    @Override
    public RequestEx2ndPasswordCheckDto deserialize(ByteBuf buffer) {
        return RequestEx2ndPasswordCheckDto.INSTANCE;
    }

}
