package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.l2.command.network.CheckProtocolVersionCommand;
import ru.finex.ws.l2.network.model.dto.ProtocolVersionDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x0E), command = @Cmd(CheckProtocolVersionCommand.class))
public final class ProtocolVersionDeserializer implements PacketDeserializer<ProtocolVersionDto> {

	@Override
	public ProtocolVersionDto deserialize(ByteBuf buffer) {
		return new ProtocolVersionDto(buffer.readIntLE());
	}

}
