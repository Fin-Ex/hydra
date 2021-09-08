package ru.finex.ws.l2.network.model;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.commons.mmocore.ReceivablePacket;

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

	public abstract <T extends NetworkDto> T getDto();

	/**
	 * @return A String with this packet name for debuging purposes
	 */
	public String getType() {
		return "[C] " + getClass().getSimpleName();
	}

}