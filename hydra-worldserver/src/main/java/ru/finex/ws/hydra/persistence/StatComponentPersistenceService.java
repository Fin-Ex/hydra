package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.StatComponentEntity;
import ru.finex.ws.hydra.repository.StatComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StatComponentPersistenceService
    extends AbstractComponentPersistenceService<StatComponentEntity, StatComponentRepository>
    implements PersistenceService<StatComponentEntity> {

    @Inject
    public StatComponentPersistenceService(StatComponentRepository repository) {
        super(repository);
    }

}
