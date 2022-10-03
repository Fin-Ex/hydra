package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.ParameterComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface ParameterComponentRepository extends CrudRepository<ParameterComponentEntity, Integer> {

    @Query("SELECT parameter FROM ParameterComponentEntity parameter WHERE parameter.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<ParameterComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
