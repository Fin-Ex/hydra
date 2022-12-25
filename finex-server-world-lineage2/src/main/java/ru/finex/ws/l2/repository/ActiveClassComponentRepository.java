package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.ActiveClassComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface ActiveClassComponentRepository extends CrudRepository<ActiveClassComponentEntity, Integer> {

    @Query("SELECT e FROM ActiveClassComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<ActiveClassComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
