package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.ChangeMoveTypeCommand;
import ru.finex.ws.l2.network.model.dto.ChangeMoveTypeDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x35), command = @Cmd(ChangeMoveTypeCommand.class))
public class ChangeMoveTypeDeserializer implements PacketDeserializer<ChangeMoveTypeDto> {

    @Override
    public ChangeMoveTypeDto deserialize(ByteBuf buffer) {
        return ChangeMoveTypeDto.builder()
            .isRunning(buffer.readIntLE() != 0)
            .build();
    }

}
