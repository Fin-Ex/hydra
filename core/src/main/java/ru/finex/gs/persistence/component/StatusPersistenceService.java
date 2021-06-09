package ru.finex.gs.persistence.component;

import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.service.persistence.PersistenceService;

/**
 * @author m0nster.mind
 */
public class StatusPersistenceService implements PersistenceService<StatusEntity> {
    @Override
    public StatusEntity persist(StatusEntity entity) {
        return entity;
    }

    @Override
    public StatusEntity restore(int gameObjectPersistenceId) {
        return StatusEntity.builder()
            .hp(1)
            .mp(1)
            .build();
    }
}
