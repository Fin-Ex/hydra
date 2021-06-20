package ru.finex.gs.proto.helios.income;

import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.GameObjectFactory;
import ru.finex.gs.proto.helios.OutcomePacketBuilderService;
import ru.finex.gs.proto.network.GameClientState;
import ru.finex.gs.proto.network.IncomePacket;
import ru.finex.gs.proto.network.L2GameClient;
import ru.finex.gs.proto.network.L2GameClientPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.service.WorldService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@IncomePacket(@Opcode(0x0d))
public class RequestGameStart extends L2GameClientPacket {

	private int selectedAvatar;
	private int unk1;
	private int unk2;
	private int unk3;
	private int unk4;

	@Inject private GameObjectFactory gameObjectFactory;
	@Inject private WorldService worldService;
	@Inject private OutcomePacketBuilderService packetBuilderService;

	@Override
	protected void readImpl() {
		selectedAvatar = readD();
		unk1 = readH();
		unk2 = readD();
		unk3 = readD();
		unk4 = readD();
	}

	@Override
	protected void runImpl() {
		L2GameClient client = getClient();

		GameObject player = gameObjectFactory.createPlayer(client, 1);
		client.setGameObject(player);

		client.setState(GameClientState.IN_GAME);
		sendPacket(packetBuilderService.characterSelected(player));
	}
}
