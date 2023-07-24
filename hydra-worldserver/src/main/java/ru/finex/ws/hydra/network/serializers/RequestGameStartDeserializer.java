package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.hydra.command.network.GameStartCommand;
import ru.finex.ws.hydra.network.model.dto.SelectedAvatarDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x12), command = @Cmd(GameStartCommand.class))
public class RequestGameStartDeserializer implements PacketDeserializer<SelectedAvatarDto> {

    @Override
    public SelectedAvatarDto deserialize(ByteBuf buffer) {
        // dhddd
        return new SelectedAvatarDto(buffer.readIntLE());
    }

}
