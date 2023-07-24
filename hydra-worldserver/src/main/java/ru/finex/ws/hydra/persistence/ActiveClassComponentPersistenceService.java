package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.ActiveClassComponentEntity;
import ru.finex.ws.hydra.repository.ActiveClassComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ActiveClassComponentPersistenceService
    extends AbstractComponentPersistenceService<ActiveClassComponentEntity, ActiveClassComponentRepository>
    implements PersistenceService<ActiveClassComponentEntity> {

    @Inject
    public ActiveClassComponentPersistenceService(ActiveClassComponentRepository repository) {
        super(repository);
    }

}
