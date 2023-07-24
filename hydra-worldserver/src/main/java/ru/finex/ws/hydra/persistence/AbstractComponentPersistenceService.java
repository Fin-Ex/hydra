package ru.finex.ws.hydra.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.core.repository.CrudRepository;
import ru.finex.ws.hydra.model.entity.GameObjectRelation;
import ru.finex.ws.hydra.repository.GameObjectRelationRepository;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public abstract class AbstractComponentPersistenceService<T extends EntityObject<Integer> & GameObjectRelation,
    R extends GameObjectRelationRepository<T> & CrudRepository<T, Integer>> implements PersistenceService<T> {

    private final R repository;

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
