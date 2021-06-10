package ru.finex.gs.service.persistence;

import ru.finex.core.model.entity.Entity;
import ru.finex.gs.model.GameObject;

/**
 * @author m0nster.mind
 */
public interface PersistenceService<T extends Entity> {

    T persist(T entity);
    T restore(int gameObjectPersistenceId);
    default T restore(GameObject gameObject) {
        return restore(gameObject.getPersistenceId());
    }

}
