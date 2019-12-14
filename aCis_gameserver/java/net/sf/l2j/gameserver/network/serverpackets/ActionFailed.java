package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public final class ActionFailed extends L2GameServerPacket {

	public static final ActionFailed STATIC_PACKET = new ActionFailed();

	private ActionFailed() {
	}

	@Override
	protected void writeImpl() {
		writeC(0x25);
	}
}
