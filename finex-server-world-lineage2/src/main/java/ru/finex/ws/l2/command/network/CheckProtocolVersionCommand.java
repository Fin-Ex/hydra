package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.ws.l2.network.AbstractNetworkCommand;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.dto.ProtocolVersionDto;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class CheckProtocolVersionCommand extends AbstractNetworkCommand {

    private final ProtocolVersionDto dto;

    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        L2GameClient client = (L2GameClient) getClient();
        if (dto.getVersion() == -2) {
            client.close(null);
        }

        // FIXME m0nster.mind: check protocol version

        client.sendPacket(packetBuilderService.keyPacket(client.enableCrypt()));
    }

}
