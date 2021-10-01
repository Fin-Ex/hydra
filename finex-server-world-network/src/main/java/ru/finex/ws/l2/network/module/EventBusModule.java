package ru.finex.ws.l2.network.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import ru.finex.core.events.EventBus;
import ru.finex.core.events.local.LocalEventBus;
import ru.finex.core.inject.LoaderModule;
import ru.finex.ws.l2.network.model.event.ClientEvent;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class EventBusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<EventBus<ClientEvent>>() {}).annotatedWith(Names.named("Network")).toInstance(new LocalEventBus<>());
    }

}
