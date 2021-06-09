package sf.l2j.gameserver.network.clientpackets;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.commons.mmocore.ReceivablePacket;
import sf.l2j.gameserver.network.L2GameClient;
import sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Packets received by the game server from clients
 *
 * @author KenM
 */
@Slf4j
public abstract class L2GameClientPacket extends ReceivablePacket<L2GameClient> {

	@Override
	protected boolean read() {
		try {
			readImpl();
			return true;
		} catch (Exception e) {
			log.error("Client: " + getClient().toString() + " - Failed reading: " + getType() + " ; " + e, e);
		}
		return false;
	}

	protected abstract void readImpl();

	@Override
	public void run() {
		try {
			runImpl();
		} catch (Throwable t) {
			log.error("Client: " + getClient().toString() + " - Failed reading: " + getType() + " ; " + t, t);
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

}
