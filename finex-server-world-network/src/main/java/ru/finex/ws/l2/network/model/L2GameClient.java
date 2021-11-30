package ru.finex.ws.l2.network.model;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.network.BlowFishKeygen;
import ru.finex.ws.l2.network.PacketExecutor;
import ru.finex.ws.l2.network.model.event.ClientDisconnected;
import ru.finex.ws.l2.network.model.event.ClientEvent;
import ru.finex.ws.model.ClientSession;

import java.net.SocketAddress;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a client connected on Game Server
 *
 * @author KenM
 */
@Slf4j
public final class L2GameClient extends SimpleChannelInboundHandler<NetworkDto> implements ClientSession {

	@Getter private GameClientState state = GameClientState.NOT_CONNECTED;

	@Getter @Setter private String login;

//	@Getter @Setter private SessionKey sessionId;

	@Getter private boolean isAuthedGG;
	@Getter @Setter private boolean isDetached;

	@Getter @Setter
	private GameObject gameObject;

	private Channel channel;

	@Inject @Named("Network")
	private EventBus<ClientEvent> eventBus;

	@Inject
	private PacketExecutor packetExecutor;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		channel = ctx.channel();
		state = GameClientState.CONNECTED;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		state = GameClientState.NOT_CONNECTED;
		eventBus.notify(new ClientDisconnected(this));
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NetworkDto msg) throws Exception {
		packetExecutor.execute(this, msg);
	}

	public byte[] enableCrypt() {
		byte[] key = BlowFishKeygen.getRandomKey();
		ChannelHandler crypt = channel.pipeline().get("crypt");
		crypt.setKey(key);
		return key;
	}

	public void setState(GameClientState pState) {
		if (state != pState) {
			state = pState;
			// FIXME m0nster.mind: clear packet queue
		}
	}

	public void setGameGuardOk(boolean val) {
		isAuthedGG = val;
	}

	public void sendPacket(NetworkDto dto) {
		if (isDetached) {
			return;
		}

		channel.writeAndFlush(dto);
	}

	public void close(NetworkDto dto) {
		isDetached = true;
		channel.writeAndFlush(dto);
		channel.close();
	}

	@SneakyThrows
	@Override
	public void closeNow() {
		isDetached = true;
		channel.close().sync();
	}

	@Override
	public String toString() {
		final SocketAddress address = channel.remoteAddress();
		switch (getState()) {
			case NOT_CONNECTED:
				return "[IP: disconnected]";
			case CONNECTED:
				return "[IP: " + address.toString() + "]";
			case AUTHED:
			case IN_GAME:
				return "[Account: " + getLogin() + " - IP: " + address.toString() + "]";
			default:
				throw new IllegalStateException("Missing state on switch");
		}
	}

}
