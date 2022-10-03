package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.repository.PlayerComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlayerComponentPersistenceService implements PersistenceService<PlayerComponentEntity> {

    private final PlayerComponentRepository repository;

    @Override
    public PlayerComponentEntity persist(PlayerComponentEntity entity) {
        return entity.getPersistenceId() == null ? repository.create(entity) : repository.update(entity);
    }

    @Override
    public PlayerComponentEntity restore(int gameObjectPersistenceId, PlayerComponentEntity entity) {
        return repository.findByGameObjectPersistenceId(gameObjectPersistenceId)
            .orElse(entity);
    }

}
