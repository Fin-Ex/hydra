package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class NormalCamera extends L2GameServerPacket {

	public static final NormalCamera STATIC_PACKET = new NormalCamera();

	private NormalCamera() {
	}

	@Override
	public void writeImpl() {
		writeC(0xc8);
	}
}
