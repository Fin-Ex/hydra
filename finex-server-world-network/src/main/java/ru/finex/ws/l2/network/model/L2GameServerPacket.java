package ru.finex.ws.l2.network.model;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.commons.mmocore.SendablePacket;

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

	protected final void writeD(boolean value) {
		this._buf.putInt(value ? 0x01 : 0x00);
	}

	public String getType() {
		return "[S] " + getClass().getSimpleName();
	}
}
