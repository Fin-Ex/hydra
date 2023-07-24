package ru.finex.ws.hydra.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.hydra.model.entity.ActiveClassComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface ActiveClassComponentRepository extends CrudRepository<ActiveClassComponentEntity, Integer>,
    GameObjectRelationRepository<ActiveClassComponentEntity> {

    @Query("SELECT e FROM ActiveClassComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<ActiveClassComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
