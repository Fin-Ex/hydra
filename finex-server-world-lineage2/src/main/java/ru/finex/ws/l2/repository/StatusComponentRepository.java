package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.StatusComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface StatusComponentRepository extends CrudRepository<StatusComponentEntity, Integer>,
    GameObjectRelationRepository<StatusComponentEntity> {

    @Query("SELECT e FROM StatusComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<StatusComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
