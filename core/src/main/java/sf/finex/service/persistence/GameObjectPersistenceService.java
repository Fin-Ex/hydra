package sf.finex.service.persistence;

import sf.finex.model.GameObject;

/**
 * Сервис для управления персистенцией игрового объекта.
 *
 * @author m0nster.mind
 */
public interface GameObjectPersistenceService {

    /**
     * Сохранить игровой объект в БД.
     *
     * @param gameObject игровой объект
     */
    void persist(GameObject gameObject);

}
