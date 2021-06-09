package sf.l2j.gameserver.network.serverpackets;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.commons.mmocore.SendablePacket;
import sf.l2j.gameserver.network.L2GameClient;

/**
 * @author KenM
 */
@Slf4j
public abstract class L2GameServerPacket extends SendablePacket<L2GameClient> {

	@Override
	protected void write() {
		try {
			writeImpl();
		} catch (Throwable t) {
			log.error("Client: " + getClient().toString() + " - Failed writing: " + getType(), t);
		}
	}

	public void runImpl() {
	}

	protected abstract void writeImpl();

	public String getType() {
		return "[S] " + getClass().getSimpleName();
	}
}
