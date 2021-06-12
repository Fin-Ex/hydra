package ru.finex.gs.proto.interlude.income;

import ru.finex.gs.proto.interlude.OutcomePacketBuilderService;
import ru.finex.gs.proto.network.IncomePacket;
import ru.finex.gs.proto.network.L2GameClientPacket;
import ru.finex.gs.proto.network.Opcode;

import javax.inject.Inject;

/**
 * @author finfan
 */
@IncomePacket({@Opcode(0xd0), @Opcode(0x08)})
public class RequestManorList extends L2GameClientPacket {
	
	@Inject
	private OutcomePacketBuilderService packetBuilderService;
	
	@Override
	protected void readImpl() {
	}
	
	@Override
	protected void runImpl() {
		sendPacket(packetBuilderService.castleManorList());
	}
	
}
