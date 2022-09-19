package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RefreshManorListCommand extends AbstractNetworkCommand {

    private final GameClient session;

    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        session.sendPacket(packetBuilderService.castleManorList());
    }
}
