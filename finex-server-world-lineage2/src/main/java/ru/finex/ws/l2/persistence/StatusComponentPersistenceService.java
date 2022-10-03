package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.StatusComponentEntity;
import ru.finex.ws.l2.repository.StatusComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class StatusComponentPersistenceService implements PersistenceService<StatusComponentEntity> {

    private final StatusComponentRepository repository;

    @Override
    public StatusComponentEntity persist(StatusComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public StatusComponentEntity restore(int gameObjectPersistenceId, StatusComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }
}
