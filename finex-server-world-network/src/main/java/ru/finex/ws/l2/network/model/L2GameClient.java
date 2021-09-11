package ru.finex.ws.l2.network.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.network.BlowFishKeygen;
import ru.finex.ws.l2.network.GameCrypt;
import ru.finex.ws.l2.network.model.event.ClientDisconnected;
import ru.finex.ws.model.Client;
import sf.l2j.commons.crypt.SessionKey;
import sf.l2j.commons.mmocore.MMOClient;
import sf.l2j.commons.mmocore.MMOConnection;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a client connected on Game Server
 *
 * @author KenM
 */
@Slf4j
public final class L2GameClient extends MMOClient implements Client {

	@Getter private GameClientState state = GameClientState.CONNECTED;

	@Getter @Setter private String login;

	@Getter @Setter private SessionKey sessionId;
	@Getter @Setter private GameCrypt crypt = new GameCrypt();

	@Getter private boolean isAuthedGG;
	@Getter @Setter private boolean isDetached;

	@Getter @Setter
	private GameObject gameObject;

	@Inject @Named("Network")
	private EventBus eventBus;

	public L2GameClient(MMOConnection<L2GameClient> con) {
		super(con);
	}

	public byte[] enableCrypt() {
		byte[] key = BlowFishKeygen.getRandomKey();
		crypt.setKey(key);
		return key;
	}

	public void setState(GameClientState pState) {
		if (state != pState) {
			state = pState;
			// FIXME m0nster.mind: clear packet queue
		}
	}

	@Override
	public boolean decrypt(ByteBuffer buf, int size) {
		crypt.decrypt(buf.array(), buf.position(), size);
		return true;
	}

	@Override
	public boolean encrypt(final ByteBuffer buf, final int size) {
		crypt.encrypt(buf.array(), buf.position(), size);
		buf.position(buf.position() + size);
		return true;
	}

	public void setGameGuardOk(boolean val) {
		isAuthedGG = val;
	}

	public void sendPacket(L2GameServerPacket gsp) {
		if (isDetached) {
			return;
		}

		getConnection().sendPacket(gsp);
		gsp.runImpl();
	}

	public void close(L2GameServerPacket gsp) {
		isDetached = true;
		getConnection().close(gsp);
	}

	@Override
	public void closeNow() {
		isDetached = true;
		getConnection().close();
	}

	@Override
	protected void onForcedDisconnection() {
		log.info("Client {} disconnected abnormally.", toString());
	}

	@Override
	protected void onDisconnection() {
		eventBus.notify(new ClientDisconnected(this));
	}

	/**
	 * Produces the best possible string representation of this client.
	 */
	@Override
	public String toString() {
		try {
			final InetAddress address = getConnection().getInetAddress();
			switch (getState()) {
				case CONNECTED:
					return "[IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
				case AUTHED:
				case IN_GAME:
					return "[Account: " + getLogin() + " - IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
				default:
					throw new IllegalStateException("Missing state on switch");
			}
		} catch (NullPointerException e) {
			return "[Character read failed due to disconnect]";
		}
	}

}
