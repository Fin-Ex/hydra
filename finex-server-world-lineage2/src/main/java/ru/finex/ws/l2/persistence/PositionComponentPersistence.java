package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.PositionComponentEntity;
import ru.finex.ws.l2.repository.PositionComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PositionComponentPersistence implements PersistenceService<PositionComponentEntity> {

    private final PositionComponentRepository repository;

    @Override
    public PositionComponentEntity persist(PositionComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public PositionComponentEntity restore(int gameObjectPersistenceId, PositionComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }
}
