package ru.finex.ws.hydra.service.component;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.ws.hydra.component.PlayerComponent;
import ru.finex.ws.hydra.model.entity.PlayerComponentEntity;
import ru.finex.ws.hydra.repository.PlayerComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 * @see PlayerComponent PlayerComponent
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlayerService {

    private final PlayerComponentRepository repository;
    private final ComponentService componentService;

    public boolean isExists(String name) {
        return repository.existsName(name);
    }

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
