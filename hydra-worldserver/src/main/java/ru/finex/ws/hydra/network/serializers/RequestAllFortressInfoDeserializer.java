package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.hydra.command.network.AllFortressInfoCommand;
import ru.finex.ws.hydra.network.model.dto.VoidDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0x3a)}, command = @Cmd(AllFortressInfoCommand.class))
public class RequestAllFortressInfoDeserializer implements PacketDeserializer<VoidDto> {

    @Override
    public VoidDto deserialize(ByteBuf buffer) {
        return null;
    }

}
