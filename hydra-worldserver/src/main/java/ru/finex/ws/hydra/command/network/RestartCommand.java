package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.model.enums.RestartReason;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.hydra.network.session.GameClientState;

import javax.inject.Inject;

/**
 * @author finfan
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RestartCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final GameClient session;

	private final OutcomePacketBuilderService packets;

	@Override
	public void executeCommand() {
		session.setState(GameClientState.AUTHED);
		session.sendPacket(packets.restart(RestartReason.OK)); //fixme: false must be if we can't restart (we in battle/flying, etc...)
		session.sendPacket(packets.charSelectInfo(session.getLogin(), session.getData().getSessionId()));
	}
}
