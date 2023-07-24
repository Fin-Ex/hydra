package ru.finex.ws.hydra.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.hydra.model.entity.StatComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface StatComponentRepository extends CrudRepository<StatComponentEntity, Integer>,
    GameObjectRelationRepository<StatComponentEntity> {

    @Query("SELECT e FROM StatComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<StatComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
