package net.sf.l2j.gameserver.network.gameserverpackets;

import org.slf4j.LoggerFactory;

public class ChangeAccessLevel extends GameServerBasePacket {

	public ChangeAccessLevel(String player, int access) {
		writeC(0x04);
		writeD(access);
		writeS(player);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
