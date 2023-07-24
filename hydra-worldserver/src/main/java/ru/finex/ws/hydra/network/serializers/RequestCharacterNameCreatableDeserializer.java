package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.hydra.command.network.CheckAvatarNameCommand;
import ru.finex.ws.hydra.network.SerializerHelper;
import ru.finex.ws.hydra.network.model.dto.RequestCharacterNameCreatableDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0xa9)}, command = @Cmd(CheckAvatarNameCommand.class))
public class RequestCharacterNameCreatableDeserializer implements PacketDeserializer<RequestCharacterNameCreatableDto> {

    @Override
    public RequestCharacterNameCreatableDto deserialize(ByteBuf buffer) {
        return new RequestCharacterNameCreatableDto(SerializerHelper.readStringNullTerm(buffer));
    }

}
