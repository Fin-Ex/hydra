package ru.finex.ws.l2.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.StateComponentEntity;
import ru.finex.ws.l2.repository.StateComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StateComponentPersistenceService extends AbstractComponentPersistenceService<StateComponentEntity>
    implements PersistenceService<StateComponentEntity> {

    @Inject
    public StateComponentPersistenceService(StateComponentRepository repository) {
        super(repository);
    }

}
