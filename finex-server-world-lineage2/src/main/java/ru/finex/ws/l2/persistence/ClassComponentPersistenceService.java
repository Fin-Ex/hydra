package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.ActiveClassComponentEntity;
import ru.finex.ws.l2.repository.ActiveClassComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject})
public class ClassComponentPersistenceService implements PersistenceService<ActiveClassComponentEntity> {

    private final ActiveClassComponentRepository repository;

    @Override
    public ActiveClassComponentEntity persist(ActiveClassComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public ActiveClassComponentEntity restore(int gameObjectPersistenceId, ActiveClassComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }

}
