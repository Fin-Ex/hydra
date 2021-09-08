package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.ws.l2.network.AbstractNetworkCommand;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.L2GameClient;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class RefreshManorListCommand extends AbstractNetworkCommand {

    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        L2GameClient client = (L2GameClient) getClient();
        client.sendPacket(packetBuilderService.castleManorList());
    }
}
