package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.command.network.RequestServerListCommand;
import ru.finex.auth.l2.network.model.dto.RequestServerListDto;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;

/**
 * @author m0nster.mind
 */
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
