package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.component.player.PlayerComponent;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.SelectedAvatarDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.session.GameClientState;
import ru.finex.ws.service.GameObjectService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameStartCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final SelectedAvatarDto dto;
    @ToString.Include
    private final GameClient session;

    private final GameObjectService gameObjectService;
    private final ComponentService componentService;
    private final OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        GameObject gameObject = gameObjectService.createGameObject("test_player", 1);
        ClientComponent component = componentService.getComponent(gameObject, ClientComponent.class);
        component.setClient(session);
        PlayerComponent player = componentService.getComponent(gameObject, PlayerComponent.class);
        player.setLogin(session.getLogin());
        player.getEntity().setName("test");

        session.setGameObject(gameObject);

        session.setState(GameClientState.IN_GAME);
        session.sendPacket(packetBuilderService.characterSelected(gameObject));
    }
}
