package ru.finex.ws.hydra.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.hydra.model.entity.ParameterComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface ParameterComponentRepository extends CrudRepository<ParameterComponentEntity, Integer>,
    GameObjectRelationRepository<ParameterComponentEntity> {

    @Query("SELECT e FROM ParameterComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<ParameterComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
