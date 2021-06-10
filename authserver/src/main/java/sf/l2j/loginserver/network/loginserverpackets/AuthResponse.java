package sf.l2j.loginserver.network.loginserverpackets;

import sf.l2j.loginserver.GameServerTable;
import sf.l2j.loginserver.network.serverpackets.ServerBasePacket;

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
