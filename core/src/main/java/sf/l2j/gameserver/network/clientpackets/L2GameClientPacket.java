package sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import java.nio.BufferUnderflowException;

import org.slf4j.Logger;

import sf.l2j.commons.mmocore.ReceivablePacket;

import sf.l2j.Config;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.L2GameClient;
import sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Packets received by the game server from clients
 *
 * @author KenM
 */
public abstract class L2GameClientPacket extends ReceivablePacket<L2GameClient> {

	protected static final Logger _log = LoggerFactory.getLogger(L2GameClientPacket.class.getName());

	@Override
	protected boolean read() {
		if (Config.PACKET_HANDLER_DEBUG) {
			_log.info(getType());
		}

		try {
			readImpl();
			return true;
		} catch (Exception e) {
			_log.error("Client: " + getClient().toString() + " - Failed reading: " + getType() + " ; " + e, e);

			if (e instanceof BufferUnderflowException) // only one allowed per client per minute
			{
				getClient().onBufferUnderflow();
			}
		}
		return false;
	}

	protected abstract void readImpl();

	@Override
	public void run() {
		try {
			runImpl();

			// Depending of the packet send, removes spawn protection
			if (triggersOnActionRequest()) {
				final Player actor = getClient().getActiveChar();
				if (actor != null && actor.isSpawnProtected()) {
					actor.onActionRequest();
					if (Config.DEBUG) {
						_log.info("Spawn protection for player " + actor.getName() + " removed by packet: " + getType());
					}
				}
			}
		} catch (Throwable t) {
			_log.error("Client: " + getClient().toString() + " - Failed reading: " + getType() + " ; " + t, t);

			if (this instanceof EnterWorld) {
				getClient().closeNow();
			}
		}
	}

	protected abstract void runImpl();

	protected final void sendPacket(L2GameServerPacket gsp) {
		getClient().sendPacket(gsp);
	}

	/**
	 * @return A String with this packet name for debuging purposes
	 */
	public String getType() {
		return "[C] " + getClass().getSimpleName();
	}

	/**
	 * Overriden with true value on some packets that should disable spawn
	 * protection
	 *
	 * @return
	 */
	protected boolean triggersOnActionRequest() {
		return true;
	}
}
