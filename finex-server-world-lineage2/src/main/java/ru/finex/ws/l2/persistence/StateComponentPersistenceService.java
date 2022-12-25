package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.StateComponentEntity;
import ru.finex.ws.l2.repository.StateComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class StateComponentPersistenceService implements PersistenceService<StateComponentEntity> {

    private final StateComponentRepository repository;

    @Override
    public StateComponentEntity persist(StateComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public StateComponentEntity restore(int gameObjectPersistenceId, StateComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }
}
