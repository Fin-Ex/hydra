package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.StatusComponentEntity;
import ru.finex.ws.hydra.repository.StatusComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StatusComponentPersistenceService
    extends AbstractComponentPersistenceService<StatusComponentEntity, StatusComponentRepository>
    implements PersistenceService<StatusComponentEntity> {

    @Inject
    public StatusComponentPersistenceService(StatusComponentRepository repository) {
        super(repository);
    }

}
