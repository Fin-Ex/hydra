package ru.finex.gs.proto.module;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.gs.proto.interlude.AuthStateProcessor;
import ru.finex.gs.proto.interlude.OutcomePacketBuilderService;
import ru.finex.gs.proto.network.ClientServiceImpl;
import ru.finex.gs.proto.network.PacketService;
import ru.finex.gs.proto.network.SelectorThreadProvider;
import ru.finex.gs.service.ClientService;
import ru.finex.nif.SelectorThread;
import sf.l2j.commons.mmocore.IPacketHandler;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthStateProcessor.class).asEagerSingleton();
        bind(PacketService.class);
        bind(IPacketHandler.class).to(PacketService.class);
        bind(OutcomePacketBuilderService.class);
        bind(SelectorThread.class).toProvider(SelectorThreadProvider.class);
        bind(ClientService.class).to(ClientServiceImpl.class);
    }

}
