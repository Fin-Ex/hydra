package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.session.GameClientState;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.SelectedAvatarDto;
import ru.finex.ws.service.GameObjectService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameStartCommand extends AbstractNetworkCommand {

    private final SelectedAvatarDto dto;
    private final GameClient session;

    @Inject private GameObjectService gameObjectService;
    @Inject private ComponentService componentService;
    @Inject private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        GameObject player = gameObjectService.createGameObject("test_player", 1);
        ClientComponent component = componentService.getComponent(player, ClientComponent.class);
        component.setClient(session);
        session.setGameObject(player);

        session.setState(GameClientState.IN_GAME);
        session.sendPacket(packetBuilderService.characterSelected(player));
    }
}
