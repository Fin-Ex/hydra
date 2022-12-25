package ru.finex.ws.l2.persistence;

import ru.finex.core.persistence.PersistenceService;
import ru.finex.ws.l2.model.entity.ClanComponentEntity;
import ru.finex.ws.l2.repository.ClanComponentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClanComponentPersistenceService extends AbstractComponentPersistenceService<ClanComponentEntity>
    implements PersistenceService<ClanComponentEntity> {

    @Inject
    public ClanComponentPersistenceService(ClanComponentRepository repository) {
        super(repository);
    }

}
