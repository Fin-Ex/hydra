package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.GameObjectRelation;
import ru.finex.ws.l2.repository.GameObjectRelationRepository;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public abstract class AbstractComponentPersistenceService<T extends EntityObject & GameObjectRelation> implements PersistenceService<T> {

    private final GameObjectRelationRepository<T> repository;

    @Override
    public T persist(T entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public T restore(int gameObjectPersistenceId, T entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }
    
}
