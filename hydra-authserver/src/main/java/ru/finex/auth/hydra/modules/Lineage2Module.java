package ru.finex.auth.hydra.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.mapstruct.factory.Mappers;
import ru.finex.auth.hydra.network.EventLoopGroupService;
import ru.finex.auth.hydra.network.NetworkAddressProvider;
import ru.finex.auth.hydra.network.NetworkPipeline;
import ru.finex.auth.hydra.network.NetworkServiceProvider;
import ru.finex.auth.hydra.network.codec.OpcodeCodecImpl;
import ru.finex.auth.hydra.network.model.event.SessionEvent;
import ru.finex.auth.hydra.populator.ServerDataPopulator;
import ru.finex.auth.hydra.service.CommandExecutorService;
import ru.finex.auth.hydra.service.SessionService;
import ru.finex.core.events.EventBus;
import ru.finex.core.events.local.LocalEventBus;
import ru.finex.core.inject.LoaderModule;
import ru.finex.network.netty.model.AbstractNetworkPipeline;
import ru.finex.network.netty.serial.OpcodeCodec;
import ru.finex.network.netty.service.NettyNetworkService;

import java.net.InetSocketAddress;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class Lineage2Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionService.class).asEagerSingleton();
        bind(CommandExecutorService.class).asEagerSingleton();

        bind(ServerDataPopulator.class).toInstance(Mappers.getMapper(ServerDataPopulator.class));

        bind(EventLoopGroupService.class).annotatedWith(Names.named("ClientNetwork")).to(EventLoopGroupService.class).in(Scopes.SINGLETON);
        bind(AbstractNetworkPipeline.class).annotatedWith(Names.named("ClientNetwork")).to(NetworkPipeline.class).in(Scopes.SINGLETON);
        bind(InetSocketAddress.class).annotatedWith(Names.named("ClientNetwork")).toProvider(NetworkAddressProvider.class).in(Scopes.SINGLETON);
        bind(NettyNetworkService.class).annotatedWith(Names.named("ClientNetwork")).toProvider(NetworkServiceProvider.class).in(Scopes.SINGLETON);
        bind(OpcodeCodec.class).to(OpcodeCodecImpl.class);
        bind(new TypeLiteral<EventBus<SessionEvent>>() { }).toInstance(new LocalEventBus<>());
    }

}
