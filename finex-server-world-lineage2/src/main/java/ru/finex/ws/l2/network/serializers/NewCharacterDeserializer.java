package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.NewCharacterCommand;
import ru.finex.ws.l2.network.model.dto.NewCharacterDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x13), command = @Cmd(NewCharacterCommand.class))
public class NewCharacterDeserializer implements PacketDeserializer<NewCharacterDto> {

    @Override
    public NewCharacterDto deserialize(ByteBuf buffer) {
        return NewCharacterDto.INSTANCE;
    }

}
