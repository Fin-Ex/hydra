package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.ClanComponentEntity;
import ru.finex.ws.l2.repository.ClanComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ClanComponentPersistenceService implements PersistenceService<ClanComponentEntity> {

    private final ClanComponentRepository repository;

    @Override
    public ClanComponentEntity persist(ClanComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public ClanComponentEntity restore(int gameObjectPersistenceId, ClanComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }

}
