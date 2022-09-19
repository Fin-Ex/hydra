package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.RefreshManorListCommand;
import ru.finex.ws.l2.network.model.dto.VoidDto;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0x01)}, command = @Cmd(RefreshManorListCommand.class))
public class RequestManorListDeserializer implements PacketDeserializer<VoidDto> {

    @Override
    public VoidDto deserialize(ByteBuf buffer) {
        return null;
    }

}
