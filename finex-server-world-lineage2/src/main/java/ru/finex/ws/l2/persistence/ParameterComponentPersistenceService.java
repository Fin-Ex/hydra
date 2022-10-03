package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.ParameterComponentEntity;
import ru.finex.ws.l2.repository.ParameterComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ParameterComponentPersistenceService implements PersistenceService<ParameterComponentEntity> {

    private final ParameterComponentRepository repository;

    @Override
    public ParameterComponentEntity persist(ParameterComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public ParameterComponentEntity restore(int gameObjectPersistenceId, ParameterComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }
}
