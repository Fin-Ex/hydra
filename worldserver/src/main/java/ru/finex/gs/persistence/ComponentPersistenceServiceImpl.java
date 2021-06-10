package ru.finex.gs.persistence;

import ru.finex.gs.model.component.Component;
import ru.finex.gs.service.persistence.ComponentPersistenceService;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ComponentPersistenceServiceImpl extends ObjectPersistenceServiceImpl implements ComponentPersistenceService {

    @Override
    public void persist(Component component) {
        super.persist(component);
    }

    @Override
    public void restore(Component component) {
        super.restore(component);
        component.onRestored();
    }

}
