package sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import sf.l2j.commons.mmocore.SendablePacket;

import sf.l2j.Config;
import sf.l2j.gameserver.network.L2GameClient;

/**
 * @author KenM
 */
public abstract class L2GameServerPacket extends SendablePacket<L2GameClient> {

	protected static final Logger _log = LoggerFactory.getLogger(L2GameServerPacket.class.getName());

	@Override
	protected void write() {
		if (Config.PACKET_HANDLER_DEBUG) {
			_log.info(getType());
		}

		try {
			writeImpl();
		} catch (Throwable t) {
			_log.error("Client: " + getClient().toString() + " - Failed writing: " + getType());
			t.printStackTrace();
		}
	}

	public void runImpl() {
	}

	protected abstract void writeImpl();

	public String getType() {
		return "[S] " + getClass().getSimpleName();
	}
}
