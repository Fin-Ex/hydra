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
public interface PositionComponentRepository extends CrudRepository<PositionComponentEntity, Integer> {

    @Query("SELECT pos FROM PositionComponentEntity pos WHERE pos.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<PositionComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
