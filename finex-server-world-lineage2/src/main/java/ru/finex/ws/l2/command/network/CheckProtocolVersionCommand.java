package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.ProtocolVersionDto;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class CheckProtocolVersionCommand extends AbstractNetworkCommand {

    private final ProtocolVersionDto dto;
    private final GameClient session;

    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        if (dto.getVersion() == -2) {
            session.close(null);
        }

        // FIXME m0nster.mind: check protocol version

        session.sendPacket(packetBuilderService.keyPacket(session.enableCrypt()));
    }

}
