package sf.finex.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import sf.finex.events.EventBus;
import sf.finex.inject.LoaderModule;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class EventBusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(Names.named("Global")).in(Singleton.class);
    }

}
