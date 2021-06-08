package sf.finex.service.persistence;

import sf.finex.model.GameObject;
import sf.finex.model.entity.Entity;

/**
 * @author m0nster.mind
 */
public interface PersistenceService<T extends Entity> {

    T persist(T entity);
    T restore(GameObject gameObject);

}
