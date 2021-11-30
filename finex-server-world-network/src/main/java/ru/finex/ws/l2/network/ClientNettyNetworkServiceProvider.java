package ru.finex.ws.l2.network;

import io.netty.channel.nio.NioEventLoopGroup;
import ru.finex.network.netty.NettyNetworkService;
import ru.finex.network.netty.NettyNetworkServiceImpl;
import ru.finex.ws.l2.network.model.NetworkConfiguration;

import java.net.InetSocketAddress;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ClientNettyNetworkServiceProvider implements Provider<NettyNetworkService> {

    private final NettyNetworkService networkService;

    @Inject
    public ClientNettyNetworkServiceProvider(NetworkConfiguration networkConfiguration, NetworkPipeline pipeline) {
        networkService = new NettyNetworkServiceImpl(
            new NioEventLoopGroup(1),
            new NioEventLoopGroup(1),
            pipeline,
            new InetSocketAddress(networkConfiguration.getHostname(), networkConfiguration.getPort())
        );
    }

    @Override
    public NettyNetworkService get() {
        return networkService;
    }

}
