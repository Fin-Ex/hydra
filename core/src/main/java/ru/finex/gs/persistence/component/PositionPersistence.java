package ru.finex.gs.persistence.component;

import ru.finex.gs.model.entity.PositionEntity;
import ru.finex.gs.service.persistence.PersistenceService;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PositionPersistence implements PersistenceService<PositionEntity> {
    @Override
    public PositionEntity persist(PositionEntity entity) {
        return entity;
    }

    @Override
    public PositionEntity restore(int gameObjectPersistenceId) {
        return PositionEntity.builder()
            .x(82698)
            .y(148638)
            .z(-3473)
            .build();
    }
}
