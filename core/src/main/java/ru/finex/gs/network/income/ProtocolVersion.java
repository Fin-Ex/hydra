package ru.finex.gs.network.income;

import ru.finex.gs.network.IncomePacket;
import ru.finex.gs.network.Opcode;
import ru.finex.gs.service.OutcomePacketBuilderService;
import sf.l2j.gameserver.network.L2GameClient;
import sf.l2j.gameserver.network.clientpackets.L2GameClientPacket;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@IncomePacket(@Opcode(0x00))
public final class ProtocolVersion extends L2GameClientPacket {

	private int version;

	@Inject
	private OutcomePacketBuilderService packetBuilderService;

	@Override
	protected void readImpl() {
		version = readD();
	}

	@Override
	protected void runImpl() {
		L2GameClient client = getClient();
		if (version == -2) {
			client.close(null);
		}

		// FIXME m0nster.mind: check protocol version

		client.sendPacket(packetBuilderService.keyPacket(getClient().enableCrypt()));
	}
}
