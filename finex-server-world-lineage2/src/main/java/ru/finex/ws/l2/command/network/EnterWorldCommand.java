package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.object.GameObject;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.service.GameObjectService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class EnterWorldCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final GameClient session;

    @Inject
    private GameObjectService gameObjectService;

    @Inject
    private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        GameObject gameObject = session.getGameObject();

//        gameObjectService.getEventBus().notify(new PlayerEnterWorld(gameObject));
        session.sendPacket(packetBuilderService.userInfo(gameObject));
    }
}
