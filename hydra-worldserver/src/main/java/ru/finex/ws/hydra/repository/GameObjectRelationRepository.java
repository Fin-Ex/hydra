package ru.finex.ws.hydra.repository;

import jakarta.validation.constraints.NotNull;
import ru.finex.ws.hydra.model.entity.GameObjectRelation;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
public interface GameObjectRelationRepository<T extends GameObjectRelation> {

    Optional<T> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
