package ru.finex.ws.l2.service;

import ru.finex.core.model.entity.EntityObject;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.LocalContext;
import ru.finex.ws.l2.model.entity.GameObjectRelation;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ObjectPersistenceServiceImpl extends ru.finex.core.persistence.impl.ObjectPersistenceServiceImpl {

    @Override
    protected void persist(EntityObject entity, PersistenceService persistenceService) {
        if (entity instanceof GameObjectRelation relation && relation.getGameObjectPersistenceId() == null) {
            Integer persistenceId = (Integer) LocalContext.get().get(GameObjectPersistenceServiceImpl.CTX_PERSISTENCE_ID);
            relation.setGameObjectPersistenceId(persistenceId);
        }
        super.persist(entity, persistenceService);
    }

}
