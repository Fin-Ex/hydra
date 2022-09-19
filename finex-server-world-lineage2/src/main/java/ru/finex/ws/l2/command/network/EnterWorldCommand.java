package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.model.event.PlayerEnterWorld;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.service.GameObjectService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class EnterWorldCommand extends AbstractNetworkCommand {

    private final GameClient session;

    @Inject
    private GameObjectService gameObjectService;

    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        GameObject gameObject = session.getGameObject();

        gameObjectService.getEventBus().notify(new PlayerEnterWorld(gameObject));
        session.sendPacket(packetBuilderService.userInfo(gameObject));
    }
}
