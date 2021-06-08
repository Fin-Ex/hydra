package sf.finex.service.persistence;

import sf.finex.model.component.Component;

/**
 * Сервис для управления персистенцией компонентов.
 *
 * @author m0nster.mind
 */
public interface ComponentPersistenceService {

    /**
     * Сохранить компонент в БД.
     *
     * @param component компонент
     */
    void persist(Component component);

}
