package ru.finex.ws.l2.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.repository.PlayerComponentRepository;

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
