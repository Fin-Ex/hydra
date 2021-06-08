package sf.finex.inject.module.loader;

import com.google.inject.AbstractModule;
import sf.finex.inject.LoaderModule;
import sf.finex.persistence.ComponentPersistenceServiceImpl;
import sf.finex.persistence.GameObjectPersistenceServiceImpl;
import sf.finex.service.persistence.ComponentPersistenceService;
import sf.finex.service.persistence.GameObjectPersistenceService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ComponentPersistenceService.class).to(ComponentPersistenceServiceImpl.class);
        bind(GameObjectPersistenceService.class).to(GameObjectPersistenceServiceImpl.class);
    }

}
