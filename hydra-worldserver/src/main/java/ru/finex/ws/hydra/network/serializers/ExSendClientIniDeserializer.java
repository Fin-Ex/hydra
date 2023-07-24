package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.hydra.command.network.ExSendClientIniCommand;
import ru.finex.ws.hydra.network.SerializerHelper;
import ru.finex.ws.hydra.network.model.dto.ExSendClientIniDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0x0104)}, command = @Cmd(ExSendClientIniCommand.class))
public class ExSendClientIniDeserializer implements PacketDeserializer<ExSendClientIniDto> {

    @Override
    public ExSendClientIniDto deserialize(ByteBuf buffer) {
        buffer.skipBytes(1); // unk byte
        return new ExSendClientIniDto(SerializerHelper.readString(buffer));
    }

}
