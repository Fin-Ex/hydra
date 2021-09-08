package ru.finex.ws.l2.persistence;

import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.core.persistence.PersistenceService;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class StatusPersistenceService implements PersistenceService<StatusEntity> {
    @Override
    public StatusEntity persist(StatusEntity entity) {
        return entity;
    }

    @Override
    public StatusEntity restore(int gameObjectPersistenceId) {
        return StatusEntity.builder()
            .hp(100)
            .mp(100)
            .maxHp(100)
            .maxMp(100)
            .build();
    }
}
