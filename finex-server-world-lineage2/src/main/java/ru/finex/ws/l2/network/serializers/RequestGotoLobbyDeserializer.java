package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.GoToLobbyCommand;
import ru.finex.ws.l2.network.model.dto.RequestGotoLobbyDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0x33)}, command = @Cmd(GoToLobbyCommand.class))
public class RequestGotoLobbyDeserializer implements PacketDeserializer<RequestGotoLobbyDto> {

    @Override
    public RequestGotoLobbyDto deserialize(ByteBuf buffer) {
        return RequestGotoLobbyDto.INSTANCE;
    }

}
