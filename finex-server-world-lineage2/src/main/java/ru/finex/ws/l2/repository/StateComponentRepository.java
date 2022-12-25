package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.StateComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface StateComponentRepository extends CrudRepository<StateComponentEntity, Integer> {

    @Query("SELECT e FROM StateComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<StateComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
