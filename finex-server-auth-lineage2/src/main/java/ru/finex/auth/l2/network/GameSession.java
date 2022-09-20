package ru.finex.auth.l2.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.finex.auth.l2.network.model.event.SessionConnected;
import ru.finex.auth.l2.network.model.event.SessionDisconnected;
import ru.finex.auth.l2.network.model.event.SessionEvent;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;
import ru.finex.core.network.AbstractClientSession;
import ru.finex.core.rng.RandomProviders;
import ru.finex.network.netty.model.ClientSession;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class GameSession extends AbstractClientSession implements ClientSession {

    @Getter
    @Setter
    private GameObject gameObject;

    @Getter
    @Setter
    private int sessionId;

    @Getter
    @Setter
    private long sessionKey;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private NetworkCommandQueue commandQueue;

    @Inject
    private EventBus<SessionEvent> eventBus;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        sessionId = RandomProviders.synchronizedRandom().get().nextInt();
        eventBus.notify(new SessionConnected(this));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        eventBus.notify(new SessionDisconnected(this));
    }
}
