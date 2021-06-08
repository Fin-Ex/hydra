package sf.finex.persistence;

import lombok.RequiredArgsConstructor;
import sf.finex.model.GameObject;
import sf.finex.service.persistence.ComponentPersistenceService;
import sf.finex.service.persistence.GameObjectPersistenceService;

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

}
