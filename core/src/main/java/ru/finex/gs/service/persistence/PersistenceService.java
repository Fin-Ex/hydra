package ru.finex.gs.service.persistence;

import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.entity.Entity;

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
