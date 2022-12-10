package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.InactiveClassComponentEntity;

import java.util.List;

/**
 * @author m0nster.mind
 */
@Valid
public interface InactiveClassComponentRepository extends CrudRepository<InactiveClassComponentEntity, Integer> {

    @Query("SELECT component FROM ClassComponentEntity component WHERE component.gameObjectPersistenceId = :gameObjectPersistenceId")
    List<InactiveClassComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
