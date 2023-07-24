package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.session.GameClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class KeyMappingCommand extends AbstractNetworkCommand {

    private final GameClient session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        // TODO m0nster.mind: ExUISetting
    }

}
