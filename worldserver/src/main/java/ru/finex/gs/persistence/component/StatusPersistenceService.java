package ru.finex.gs.persistence.component;

import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.service.persistence.PersistenceService;

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
