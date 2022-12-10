package ru.finex.ws.l2.service.component;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.ws.l2.component.player.PlayerComponent;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 * @see ru.finex.ws.l2.component.player.PlayerComponent PlayerComponent
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlayerService {

    private final ComponentService componentService;

    public void setLogin(GameObject gameObject, String login) {
        PlayerComponentEntity entity = getEntity(gameObject);
        if (entity.getLogin() != null) {
            return;
        }

        entity.setLogin(login);
    }

    private PlayerComponentEntity getEntity(GameObject gameObject) {
        return componentService.getComponent(gameObject, PlayerComponent.class)
            .getEntity();
    }

}