package net.sf.l2j.loginserver.network.loginserverpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.loginserver.GameServerTable;
import net.sf.l2j.loginserver.network.serverpackets.ServerBasePacket;

public class AuthResponse extends ServerBasePacket {

	public AuthResponse(int serverId) {
		writeC(0x02);
		writeC(serverId);
		writeS(GameServerTable.getInstance().getServerNames().get(serverId));
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
