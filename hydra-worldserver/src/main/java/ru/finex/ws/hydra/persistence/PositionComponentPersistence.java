package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.PositionComponentEntity;
import ru.finex.ws.hydra.repository.PositionComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PositionComponentPersistence
    extends AbstractComponentPersistenceService<PositionComponentEntity, PositionComponentRepository>
    implements PersistenceService<PositionComponentEntity> {

    @Inject
    public PositionComponentPersistence(PositionComponentRepository repository) {
        super(repository);
    }

}
