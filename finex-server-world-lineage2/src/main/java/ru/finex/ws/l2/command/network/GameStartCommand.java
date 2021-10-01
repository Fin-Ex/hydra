package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.network.AbstractNetworkCommand;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.GameClientState;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.dto.SelectedAvatarDto;
import ru.finex.ws.service.GameObjectService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class GameStartCommand extends AbstractNetworkCommand {

    private final SelectedAvatarDto dto;

    @Inject private GameObjectService gameObjectService;
    @Inject private ComponentService componentService;
    @Inject private OutcomePacketBuilderService packetBuilderService;

    @Override
    public void executeCommand() {
        L2GameClient client = (L2GameClient) getClient();

        GameObject player = gameObjectService.createGameObject("test_player", 1);
        ClientComponent component = componentService.getComponent(player, ClientComponent.class);
        component.setClient(client);
        client.setGameObject(player);

        client.setState(GameClientState.IN_GAME);
        client.sendPacket(packetBuilderService.characterSelected(player));
    }
}
