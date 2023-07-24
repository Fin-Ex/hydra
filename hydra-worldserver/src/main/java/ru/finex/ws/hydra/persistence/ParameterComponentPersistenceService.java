package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.ParameterComponentEntity;
import ru.finex.ws.hydra.repository.ParameterComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ParameterComponentPersistenceService
    extends AbstractComponentPersistenceService<ParameterComponentEntity, ParameterComponentRepository>
    implements PersistenceService<ParameterComponentEntity> {

    @Inject
    public ParameterComponentPersistenceService(ParameterComponentRepository repository) {
        super(repository);
    }

}
