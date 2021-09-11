package ru.finex.ws.l2.network.module;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.nif.SelectorThread;
import ru.finex.ws.l2.network.ClientServiceImpl;
import ru.finex.ws.l2.network.PacketService;
import ru.finex.ws.l2.network.SelectorThreadProvider;
import ru.finex.ws.service.ClientService;
import sf.l2j.commons.mmocore.IPacketHandler;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PacketService.class);
        bind(IPacketHandler.class).to(PacketService.class);
        bind(SelectorThread.class).toProvider(SelectorThreadProvider.class);
        bind(ClientService.class).to(ClientServiceImpl.class);
    }

}
