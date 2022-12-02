package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.MoveBackwardToLocationCommand;
import ru.finex.ws.l2.command.network.ValidatePositionCommand;
import ru.finex.ws.l2.network.model.dto.MoveBackwardToLocationDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0x0F)}, command = @Cmd(MoveBackwardToLocationCommand.class))
public class MoveBackwardToLocationDeserializer implements PacketDeserializer<MoveBackwardToLocationDto> {

    @Override
    public MoveBackwardToLocationDto deserialize(ByteBuf buffer) {
        return MoveBackwardToLocationDto.builder()
            .destinationX(buffer.readIntLE())
            .destinationY(buffer.readIntLE())
            .destinationZ(buffer.readIntLE())
            .startX(buffer.readIntLE())
            .startY(buffer.readIntLE())
            .startZ(buffer.readIntLE())
            .mode(buffer.readIntLE())
            .build();
    }

}
