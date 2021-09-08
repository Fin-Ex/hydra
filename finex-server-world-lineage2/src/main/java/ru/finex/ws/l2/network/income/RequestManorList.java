package ru.finex.ws.l2.network.income;

import ru.finex.ws.l2.command.network.RefreshManorListCommand;
import ru.finex.ws.l2.network.IncomePacket;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.NetworkDto;

/**
 * @author finfan
 */
@IncomePacket(value = {@Opcode(0xd0), @Opcode(0x08)}, command = RefreshManorListCommand.class)
public class RequestManorList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	public <T extends NetworkDto> T getDto() {
		return null;
	}

}
