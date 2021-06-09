package sf.l2j.loginserver.network.serverpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fromat: d d: response
 */
public final class GGAuth extends L2LoginServerPacket {

	static final Logger _log = LoggerFactory.getLogger(GGAuth.class.getName());
	public static final int SKIP_GG_AUTH_REQUEST = 0x0b;

	private final int _response;

	public GGAuth(int response) {
		_response = response;
	}

	@Override
	protected void write() {
		writeC(0x0b);
		writeD(_response);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}
