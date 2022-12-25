package ru.finex.ws.l2.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.StatusComponentEntity;
import ru.finex.ws.l2.repository.StatusComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StatusComponentPersistenceService extends AbstractComponentPersistenceService<StatusComponentEntity>
    implements PersistenceService<StatusComponentEntity> {

    @Inject
    public StatusComponentPersistenceService(StatusComponentRepository repository) {
        super(repository);
    }

}
