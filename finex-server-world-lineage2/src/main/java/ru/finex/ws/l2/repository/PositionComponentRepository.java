package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.PositionComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface PositionComponentRepository extends CrudRepository<PositionComponentEntity, Integer>,
    GameObjectRelationRepository<PositionComponentEntity> {

    @Query("SELECT e FROM PositionComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<PositionComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
