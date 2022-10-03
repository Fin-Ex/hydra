package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.session.GameClient;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RefreshManorListCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final GameClient session;

    private final OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        session.sendPacket(packetBuilderService.castleManorList());
    }
}
