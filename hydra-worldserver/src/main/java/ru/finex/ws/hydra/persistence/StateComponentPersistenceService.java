package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.StateComponentEntity;
import ru.finex.ws.hydra.repository.StateComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StateComponentPersistenceService
    extends AbstractComponentPersistenceService<StateComponentEntity, StateComponentRepository>
    implements PersistenceService<StateComponentEntity> {

    @Inject
    public StateComponentPersistenceService(StateComponentRepository repository) {
        super(repository);
    }

}
