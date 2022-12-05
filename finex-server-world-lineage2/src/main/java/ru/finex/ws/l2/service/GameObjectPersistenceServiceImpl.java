package ru.finex.ws.l2.service;

import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.core.persistence.ObjectPersistenceService;
import ru.finex.ws.l2.LocalContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class GameObjectPersistenceServiceImpl extends ru.finex.core.persistence.impl.GameObjectPersistenceServiceImpl {

    public static final String CTX_PERSISTENCE_ID = "persistenceId";

    @Inject
    public GameObjectPersistenceServiceImpl(ObjectPersistenceService objectPersistenceService,
        ComponentService componentService) {
        super(objectPersistenceService, componentService);
    }

    @Override
    public void persist(GameObject gameObject) {
        LocalContext.get().put(CTX_PERSISTENCE_ID, gameObject.getPersistenceId());
        try {
            super.persist(gameObject);
        } finally {
            LocalContext.get().remove(CTX_PERSISTENCE_ID);
        }
    }

    @Override
    public void restore(GameObject gameObject) {
        LocalContext.get().put(CTX_PERSISTENCE_ID, gameObject.getPersistenceId());
        try {
            super.restore(gameObject);
        } finally {
            LocalContext.get().remove(CTX_PERSISTENCE_ID);
        }
    }

}
