package ru.finex.ws.hydra.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.hydra.model.entity.ClanComponentEntity;
import ru.finex.ws.hydra.repository.ClanComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClanComponentPersistenceService
    extends AbstractComponentPersistenceService<ClanComponentEntity, ClanComponentRepository>
    implements PersistenceService<ClanComponentEntity> {

    @Inject
    public ClanComponentPersistenceService(ClanComponentRepository repository) {
        super(repository);
    }

}
