package ru.finex.gs.proto.helios.income;

import ru.finex.gs.proto.helios.OutcomePacketBuilderService;
import ru.finex.gs.proto.network.IncomePacket;
import ru.finex.gs.proto.network.L2GameClient;
import ru.finex.gs.proto.network.L2GameClientPacket;
import ru.finex.gs.proto.network.Opcode;

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
