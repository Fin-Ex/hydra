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
public interface StatusComponentRepository extends CrudRepository<StatusComponentEntity, Integer> {

    @Query("SELECT status FROM StatusComponentEntity status WHERE status.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<StatusComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
