package ru.finex.gs.inject.module.loader;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.gs.persistence.ComponentPersistenceServiceImpl;
import ru.finex.gs.persistence.GameObjectPersistenceServiceImpl;
import ru.finex.gs.persistence.ObjectPersistenceServiceImpl;
import ru.finex.gs.service.persistence.ComponentPersistenceService;
import ru.finex.gs.service.persistence.GameObjectPersistenceService;
import ru.finex.gs.service.persistence.ObjectPersistenceService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectPersistenceService.class).to(ObjectPersistenceServiceImpl.class);
        bind(ComponentPersistenceService.class).to(ComponentPersistenceServiceImpl.class);
        bind(GameObjectPersistenceService.class).to(GameObjectPersistenceServiceImpl.class);
    }

}
