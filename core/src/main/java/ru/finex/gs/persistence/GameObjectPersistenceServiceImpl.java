package ru.finex.gs.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.service.persistence.ComponentPersistenceService;
import ru.finex.gs.service.persistence.GameObjectPersistenceService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectPersistenceServiceImpl implements GameObjectPersistenceService {

    private final ComponentPersistenceService componentPersistenceService;

    @Override
    public void persist(GameObject gameObject) {
        gameObject.getComponents()
            .forEach(componentPersistenceService::persist);
    }

    @Override
    public void restore(GameObject gameObject) {
        gameObject.getComponents()
            .forEach(componentPersistenceService::restore);
    }

}
