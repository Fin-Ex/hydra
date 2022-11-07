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
import ru.finex.core.network.AbstractClientSession;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.core.object.GameObject;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.transport.l2.model.dto.AuthSession;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@NetworkCommandScoped
public class GameSession extends AbstractClientSession implements ClientSession {

    @Getter
    private final AuthSession data = new AuthSession();

    @Getter
    @Setter
    private GameObject gameObject;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private NetworkCommandQueue commandQueue;

    @Inject
    private EventBus<SessionEvent> eventBus;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        eventBus.notify(new SessionConnected(this));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        eventBus.notify(new SessionDisconnected(this));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
