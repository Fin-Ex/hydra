package ru.finex.gs.proto.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.finex.core.events.EventBus;
import ru.finex.core.inject.LoaderModule;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class EventBusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(Names.named("Network")).in(Singleton.class);
    }

}
