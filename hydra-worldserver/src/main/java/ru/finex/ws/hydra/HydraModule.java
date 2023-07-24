package ru.finex.ws.hydra;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import lombok.EqualsAndHashCode;
import ru.finex.core.events.EventBus;
import ru.finex.core.events.local.LocalEventBus;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.OverrideModule;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.core.persistence.ObjectPersistenceService;
import ru.finex.evolution.Evolution;
import ru.finex.network.netty.model.AbstractNetworkPipeline;
import ru.finex.network.netty.serial.OpcodeCodec;
import ru.finex.network.netty.service.NettyNetworkService;
import ru.finex.ws.inject.module.WorldServerModule;
import ru.finex.ws.hydra.network.EventLoopGroupService;
import ru.finex.ws.hydra.network.NetworkAddressProvider;
import ru.finex.ws.hydra.network.NetworkPipeline;
import ru.finex.ws.hydra.network.NetworkServiceProvider;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.codec.OpcodeCodecImpl;
import ru.finex.ws.hydra.network.model.UserInfoComponent;
import ru.finex.ws.hydra.network.model.event.GameSessionEvent;
import ru.finex.ws.hydra.network.serializers.userinfo.UIComponentSerializer;
import ru.finex.ws.hydra.service.AutoSaveService;
import ru.finex.ws.hydra.service.ClientServiceImpl;
import ru.finex.ws.hydra.service.GameObjectPersistenceServiceImpl;
import ru.finex.ws.hydra.service.ObjectPersistenceServiceImpl;
import ru.finex.ws.hydra.service.SessionService;
import ru.finex.ws.service.ClientService;

import java.net.InetSocketAddress;
import java.util.stream.Stream;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode(callSuper = false)
@OverrideModule(WorldServerModule.class)
@Evolution(value = "hydra", dependencies = "ws")
public class HydraModule extends AbstractModule {

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

        persistenceModule();

        registerUIComponents();
    }

    private void persistenceModule() {
        bind(GameObjectPersistenceService.class).to(GameObjectPersistenceServiceImpl.class);
        bind(ObjectPersistenceService.class).to(ObjectPersistenceServiceImpl.class);
    }

    private void registerUIComponents() {
        var uiComponents = MapBinder.newMapBinder(
            binder(),
            new TypeLiteral<UserInfoComponent>() { },
            new TypeLiteral<UIComponentSerializer>() { },
            Names.named("UIComponents")
        );

        Stream.of(UserInfoComponent.values())
            .forEach(e -> uiComponents.addBinding(e).to(e.getSerializer()));
    }

}
