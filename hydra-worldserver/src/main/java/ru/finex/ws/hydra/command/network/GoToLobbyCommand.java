package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.session.GameClient;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject})
public class GoToLobbyCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final GameClient session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        session.sendPacket(packets.charSelectInfo(session.getLogin(), session.getData().getSessionId()));
    }
}
