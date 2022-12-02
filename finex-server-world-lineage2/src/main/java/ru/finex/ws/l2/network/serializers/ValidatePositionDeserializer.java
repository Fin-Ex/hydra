package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.ValidatePositionCommand;
import ru.finex.ws.l2.network.model.dto.ValidateLocationDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0x59)}, command = @Cmd(ValidatePositionCommand.class))
public class ValidatePositionDeserializer implements PacketDeserializer<ValidateLocationDto> {

    @Override
    public ValidateLocationDto deserialize(ByteBuf buffer) {
        return ValidateLocationDto.builder()
            .x(buffer.readIntLE())
            .y(buffer.readIntLE())
            .z(buffer.readIntLE())
            .heading(buffer.readIntLE())
            .vehicleId(buffer.readIntLE())
            .build();
    }

}
