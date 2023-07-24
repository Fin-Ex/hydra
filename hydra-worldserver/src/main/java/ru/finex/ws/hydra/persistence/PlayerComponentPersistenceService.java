package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.PlayerComponentEntity;
import ru.finex.ws.hydra.repository.PlayerComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PlayerComponentPersistenceService
    extends AbstractComponentPersistenceService<PlayerComponentEntity, PlayerComponentRepository>
    implements PersistenceService<PlayerComponentEntity> {

    @Inject
    public PlayerComponentPersistenceService(PlayerComponentRepository repository) {
        super(repository);
    }

}
