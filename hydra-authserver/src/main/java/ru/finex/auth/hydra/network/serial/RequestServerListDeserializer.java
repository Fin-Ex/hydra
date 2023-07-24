package ru.finex.auth.hydra.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.hydra.command.network.RequestServerListCommand;
import ru.finex.auth.hydra.network.model.dto.RequestServerListDto;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x05), command = @Cmd(RequestServerListCommand.class))
public class RequestServerListDeserializer implements PacketDeserializer<RequestServerListDto> {

    @Override
    public RequestServerListDto deserialize(ByteBuf buffer) {
        // ddc
        return RequestServerListDto.builder()
            .sessionKey(buffer.readLongLE())
            .build();
    }

}
