package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.command.network.RequestServerLoginCommand;
import ru.finex.auth.l2.network.model.dto.RequestServerLoginDto;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = @Opcode(0x02), command = @Cmd(RequestServerLoginCommand.class))
public class RequestServerLoginDeserializer implements PacketDeserializer<RequestServerLoginDto> {

    @Override
    public RequestServerLoginDto deserialize(ByteBuf buffer) {
        return RequestServerLoginDto.builder()
            .sessionKey(buffer.readLongLE())
            .serverId(buffer.readByte())
            .build();
    }

}
