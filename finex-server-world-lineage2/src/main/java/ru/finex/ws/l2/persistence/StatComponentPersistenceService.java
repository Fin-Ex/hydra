package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.StatComponentEntity;
import ru.finex.ws.l2.repository.StatComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class StatComponentPersistenceService implements PersistenceService<StatComponentEntity> {

    private final StatComponentRepository repository;

    @Override
    public StatComponentEntity persist(StatComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public StatComponentEntity restore(int gameObjectPersistenceId, StatComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }
}
