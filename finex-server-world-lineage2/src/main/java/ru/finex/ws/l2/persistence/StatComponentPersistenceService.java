package ru.finex.ws.l2.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.StatComponentEntity;
import ru.finex.ws.l2.repository.StatComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StatComponentPersistenceService extends AbstractComponentPersistenceService<StatComponentEntity>
    implements PersistenceService<StatComponentEntity> {

    @Inject
    public StatComponentPersistenceService(StatComponentRepository repository) {
        super(repository);
    }

}
