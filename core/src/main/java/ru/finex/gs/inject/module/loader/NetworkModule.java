package ru.finex.gs.inject.module.loader;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.gs.network.ClientServiceImpl;
import ru.finex.gs.network.OutcomePacketBuilderServiceImpl;
import ru.finex.gs.network.PacketServiceImpl;
import ru.finex.gs.network.SelectorThreadProvider;
import ru.finex.gs.service.ClientService;
import ru.finex.gs.service.OutcomePacketBuilderService;
import ru.finex.gs.service.PacketService;
import sf.l2j.commons.mmocore.IPacketHandler;
import sf.l2j.commons.mmocore.SelectorThread;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PacketService.class).to(PacketServiceImpl.class);
        bind(IPacketHandler.class).to(PacketServiceImpl.class);
        bind(OutcomePacketBuilderService.class).to(OutcomePacketBuilderServiceImpl.class);
        bind(SelectorThread.class).toProvider(SelectorThreadProvider.class);
        bind(ClientService.class).to(ClientServiceImpl.class);
    }

}
