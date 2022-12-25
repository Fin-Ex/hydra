package ru.finex.ws.l2.repository;

import jakarta.validation.constraints.NotNull;
import ru.finex.ws.l2.model.entity.GameObjectRelation;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
public interface GameObjectRelationRepository<T extends GameObjectRelation> {

    Optional<T> findByGameObjectPersistenceId(@NotNull Integer gameObjectPersistenceId);

}
