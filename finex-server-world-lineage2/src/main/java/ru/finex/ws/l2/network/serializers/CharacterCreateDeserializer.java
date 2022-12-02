package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.CharacterCreateCommand;
import ru.finex.ws.l2.command.network.MoveBackwardToLocationCommand;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;
import ru.finex.ws.l2.network.model.dto.MoveBackwardToLocationDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0x0C)}, command = @Cmd(CharacterCreateCommand.class))
public class CharacterCreateDeserializer implements PacketDeserializer<CharacterCreateDto> {

    @Override
    public CharacterCreateDto deserialize(ByteBuf buffer) {
        return CharacterCreateDto.builder()
            .name(SerializerHelper.readString(buffer))
            .race(buffer.readIntLE())
            .sex((byte) buffer.readIntLE())
            .classId(buffer.readIntLE())
            .race(buffer.readIntLE())
            .INT(buffer.readIntLE())
            .STR(buffer.readIntLE())
            .VIT(buffer.readIntLE())
            .SPRT(buffer.readIntLE())
            .AGI(buffer.readIntLE())
            .hairStyle((byte) buffer.readIntLE())
            .hairColor((byte) buffer.readIntLE())
            .face((byte) buffer.readIntLE())
            .build();
    }

}
