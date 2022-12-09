package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.entity.AvatarView;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.SelectedAvatarDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.session.GameClientState;
import ru.finex.ws.l2.service.AvatarService;
import ru.finex.ws.l2.service.component.PlayerService;
import ru.finex.ws.service.GameObjectService;

import java.util.List;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameStartCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final SelectedAvatarDto dto;
    @ToString.Include
    private final GameClient session;

    private final AvatarService avatarService;
    private final GameObjectService gameObjectService;
    private final ComponentService componentService;
    private final PlayerService playerService;
    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        int selectedAvatar = dto.getSelectedAvatar();
        List<AvatarView> avatars = avatarService.get(session.getLogin());
        if (selectedAvatar < 0 || selectedAvatar >= avatars.size()) {
            log.debug("Avatar not found for '{}', selection: {}", session.getLogin(), selectedAvatar);
            return; // m0nster.mind: failed packet?
        }

        AvatarView avatar = avatars.get(selectedAvatar);
        String prototypeName = avatarService.getPrototypeName(ClassId.ofId(avatar.getClassId()), avatar.getGender());
        GameObject gameObject = gameObjectService.createGameObject(prototypeName, avatar.getPersistenceId());
        ClientComponent component = componentService.getComponent(gameObject, ClientComponent.class);
        component.setClient(session);
        playerService.setLogin(gameObject, session.getLogin());

        session.setGameObject(gameObject);

        session.setState(GameClientState.IN_GAME);
        session.sendPacket(packets.characterSelected(gameObject));
    }
}
