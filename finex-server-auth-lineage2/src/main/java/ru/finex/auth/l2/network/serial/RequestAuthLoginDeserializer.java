package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.command.network.RequestAuthLoginCommand;
import ru.finex.auth.l2.network.model.dto.RequestAuthLoginDto;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
@IncomePacket(value = @Opcode(0x00), command = @Cmd(RequestAuthLoginCommand.class))
public class RequestAuthLoginDeserializer implements PacketDeserializer<RequestAuthLoginDto> {

    private final RequestNewAuthLoginDeserializer newAuthLoginDeserializer;
    private final RequestOldAuthLoginDeserializer oldAuthLoginDeserializer;

    @Override
    public RequestAuthLoginDto deserialize(ByteBuf buffer) {
        if (isNewAuthStruct(buffer)) {
            return newAuthLoginDeserializer.deserialize(buffer);
        }

        return oldAuthLoginDeserializer.deserialize(buffer);
    }

    private boolean isNewAuthStruct(ByteBuf buffer) {
        return buffer.readableBytes() >= 256;
    }

}
