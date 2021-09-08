package ru.finex.ws.l2.network.income;

import lombok.Getter;
import ru.finex.ws.l2.command.network.GameStartCommand;
import ru.finex.ws.l2.network.IncomePacket;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.dto.SelectedAvatarDto;

/**
 * @author m0nster.mind
 */
@IncomePacket(value = @Opcode(0x0d), command = GameStartCommand.class)
public class RequestGameStart extends L2GameClientPacket {

	@Getter
	private SelectedAvatarDto dto;

	@Override
	protected void readImpl() {
		dto = new SelectedAvatarDto(readD());
		readH();
		readD();
		readD();
		readD();
	}

}
