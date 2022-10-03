package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.ClanComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface ClanComponentRepository extends CrudRepository<ClanComponentEntity, Integer> {

    @Query("SELECT clan FROM ClanComponentEntity clan WHERE clan.gameObjectPersistenceId = :gameObjectPersistenceId")
    Optional<ClanComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
