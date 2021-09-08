package ru.finex.ws.l2.module;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.ws.l2.network.AuthStateProcessor;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthStateProcessor.class).asEagerSingleton();
        bind(OutcomePacketBuilderService.class);
    }

}
