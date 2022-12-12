package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.session.GameClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AllFortressInfoCommand extends AbstractNetworkCommand {

    private final GameClient session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        session.sendPacket(packets.allFortressInfo());
    }

}
