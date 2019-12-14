package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public final class KeyPacket extends L2GameServerPacket {

	private final byte[] _key;

	public KeyPacket(byte[] key) {
		_key = key;
	}

	@Override
	public void writeImpl() {
		writeC(0x00);
		writeC(0x01);
		writeB(_key);
		writeD(0x01);
		writeD(0x01);
	}
}
