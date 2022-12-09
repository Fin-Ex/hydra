package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.CharacterCreateCommand;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;

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
            .race(Race.ofId(buffer.readIntLE()))
            .gender(Gender.ofId(buffer.readIntLE()))
            .classId(buffer.readIntLE())
            .race(Race.ofId(buffer.readIntLE()))
            .INT(buffer.readIntLE())
            .STR(buffer.readIntLE())
            .VIT(buffer.readIntLE())
            .SPRT(buffer.readIntLE())
            .AGI(buffer.readIntLE())
            .hairType(buffer.readIntLE())
            .hairColor(buffer.readIntLE())
            .face(buffer.readIntLE())
            .build();
    }

}
