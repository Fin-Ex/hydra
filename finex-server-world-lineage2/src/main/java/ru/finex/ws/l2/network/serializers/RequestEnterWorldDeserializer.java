package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.EnterWorldCommand;
import ru.finex.ws.l2.network.model.dto.VoidDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x11), command = @Cmd(EnterWorldCommand.class))
public class RequestEnterWorldDeserializer implements PacketDeserializer<VoidDto> {

    @Override
    public VoidDto deserialize(ByteBuf buffer) {
        // x20 c
        // dddd
        // x64 c
        // d
        return null;
    }

}
