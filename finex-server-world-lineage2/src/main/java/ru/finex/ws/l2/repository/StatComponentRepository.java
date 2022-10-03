package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.StatComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface StatComponentRepository extends CrudRepository<StatComponentEntity, Integer> {

    @Query("SELECT stat FROM StatComponentEntity stat WHERE stat.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<StatComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
