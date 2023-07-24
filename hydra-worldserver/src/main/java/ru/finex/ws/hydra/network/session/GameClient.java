package ru.finex.ws.hydra.network.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.events.EventBus;
import ru.finex.core.network.AbstractClientSession;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.core.object.GameObject;
import ru.finex.transport.hydra.model.dto.WorldSession;
import ru.finex.ws.hydra.network.codec.PayloadCodec;
import ru.finex.ws.hydra.network.model.event.GameSessionEvent;
import ru.finex.ws.hydra.network.model.event.SessionConnected;
import ru.finex.ws.hydra.network.model.event.SessionDisconnected;
import ru.finex.ws.model.ClientSession;

import javax.inject.Inject;

/**
 * Represents a client connected on Game Server
 *
 * @author KenM
 */
@Slf4j
@NetworkCommandScoped
public final class GameClient extends AbstractClientSession implements ClientSession {

	private static final String CTX_LOGIN = "Login";

	@Getter private final WorldSession data = new WorldSession();

	@Getter @Setter private GameClientState state = GameClientState.NOT_CONNECTED;

	@Getter @Setter private boolean isAuthedGG;
	@Getter @Setter private GameObject gameObject;
	@Inject private EventBus<GameSessionEvent> eventBus;
	@Inject @Getter(AccessLevel.PROTECTED) private NetworkCommandQueue commandQueue;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		// m0nster.mind: создаем сессию сразу с авторизованным стейтом, т.к. у нас нет логин-сервера
		//state = GameClientState.CONNECTED;
		state = GameClientState.AUTHED;
		eventBus.notify(new SessionConnected(this));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		state = GameClientState.NOT_CONNECTED;
		eventBus.notify(new SessionDisconnected(this));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("Client disconnected due to unhandled exception", cause);
		closeNow();
	}

	public void setCryptKey(byte[] blowfishKey) {
		PayloadCodec crypt = (PayloadCodec) getChannel().pipeline().get("crypt");
		crypt.setKey(blowfishKey);
	}

	public String getLogin() {
		return getContext().get(CTX_LOGIN).toString();
	}

	public void setLogin(String login) {
		getContext().put(CTX_LOGIN, login);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
