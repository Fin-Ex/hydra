package ru.finex.ws.l2;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import ru.finex.core.events.EventBus;
import ru.finex.core.events.local.LocalEventBus;
import ru.finex.core.inject.LoaderModule;
import ru.finex.evolution.Evolution;
import ru.finex.network.netty.model.AbstractNetworkPipeline;
import ru.finex.network.netty.serial.OpcodeCodec;
import ru.finex.network.netty.service.NettyNetworkService;
import ru.finex.ws.l2.network.EventLoopGroupService;
import ru.finex.ws.l2.network.NetworkAddressProvider;
import ru.finex.ws.l2.network.NetworkPipeline;
import ru.finex.ws.l2.network.NetworkServiceProvider;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.codec.OpcodeCodecImpl;
import ru.finex.ws.l2.network.model.event.GameSessionEvent;
import ru.finex.ws.l2.service.AutoSaveService;
import ru.finex.ws.l2.service.ClientServiceImpl;
import ru.finex.ws.l2.service.SessionService;
import ru.finex.ws.service.ClientService;

import java.net.InetSocketAddress;

/**
 * @author m0nster.mind
 */
@LoaderModule
@Evolution(value = "lin2", dependencies = "ws")
public class Lineage2Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionService.class).asEagerSingleton();
        bind(OutcomePacketBuilderService.class);
        bind(AutoSaveService.class).asEagerSingleton();

        bind(ClientService.class).to(ClientServiceImpl.class);
        bind(EventLoopGroupService.class).annotatedWith(Names.named("ClientNetwork")).to(EventLoopGroupService.class).in(Scopes.SINGLETON);
        bind(AbstractNetworkPipeline.class).annotatedWith(Names.named("ClientNetwork")).to(NetworkPipeline.class).in(Scopes.SINGLETON);
        bind(InetSocketAddress.class).annotatedWith(Names.named("ClientNetwork")).toProvider(NetworkAddressProvider.class).in(Scopes.SINGLETON);
        bind(NettyNetworkService.class).annotatedWith(Names.named("ClientNetwork")).toProvider(NetworkServiceProvider.class).in(Scopes.SINGLETON);
        bind(OpcodeCodec.class).to(OpcodeCodecImpl.class);
        bind(new TypeLiteral<EventBus<GameSessionEvent>>() { }).toInstance(new LocalEventBus<>());
    }

}