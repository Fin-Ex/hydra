package ru.finex.ws.hydra.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.hydra.model.entity.StateComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface StateComponentRepository extends CrudRepository<StateComponentEntity, Integer>,
    GameObjectRelationRepository<StateComponentEntity> {

    @Query("SELECT e FROM StateComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<StateComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
