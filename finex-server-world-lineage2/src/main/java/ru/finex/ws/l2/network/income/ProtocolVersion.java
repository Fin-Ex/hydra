package ru.finex.ws.l2.network.income;

import lombok.Getter;
import ru.finex.ws.l2.command.network.CheckProtocolVersionCommand;
import ru.finex.ws.l2.network.IncomePacket;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.dto.ProtocolVersionDto;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = @Opcode(0x00), command = CheckProtocolVersionCommand.class)
public final class ProtocolVersion extends L2GameClientPacket {

	@Getter
	private ProtocolVersionDto dto;

	@Override
	protected void readImpl() {
		dto = new ProtocolVersionDto(readD());
	}

}
