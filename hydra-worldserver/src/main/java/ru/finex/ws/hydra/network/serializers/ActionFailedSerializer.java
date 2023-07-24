package ru.finex.ws.hydra.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.hydra.network.model.dto.ActionFailedDto;

import javax.inject.Singleton;

/**
 * @author finfan
 */
@Singleton
@OutcomePacket(@Opcode(0x1F))
public class ActionFailedSerializer implements PacketSerializer<ActionFailedDto> {

	@Override
	public void serialize(ActionFailedDto dto, ByteBuf buffer) {
		buffer.writeIntLE(dto.getType());
	}
}
