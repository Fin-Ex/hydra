package sf.l2j.loginserver.network.loginserverpackets;

import sf.l2j.loginserver.LoginServer;
import sf.l2j.loginserver.network.serverpackets.ServerBasePacket;

public class InitLS extends ServerBasePacket {

	public InitLS(byte[] publickey) {
		writeC(0x00);
		writeD(LoginServer.PROTOCOL_REV);
		writeD(publickey.length);
		writeB(publickey);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
