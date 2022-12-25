package ru.finex.ws.l2.repository;

import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Valid
public interface PlayerComponentRepository extends CrudRepository<PlayerComponentEntity, Integer>,
    GameObjectRelationRepository<PlayerComponentEntity> {

    @Query("SELECT e FROM PlayerComponentEntity e WHERE e.gameObjectPersistenceId = :gameObjectPersistenceId")
    @Override
    Optional<PlayerComponentEntity> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

    @Query("SELECT player.login FROM PlayerComponentEntity player WHERE player.login = :login")
    Long findGameObjectPersistenceIdByLogin(@NotNull String login) throws NoResultException;

    @Query("SELECT count(*) > 0 FROM PlayerComponentEntity player WHERE player.name = :name")
    Boolean existsName(String name);

}
