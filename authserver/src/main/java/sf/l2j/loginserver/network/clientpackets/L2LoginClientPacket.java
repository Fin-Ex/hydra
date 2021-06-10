package sf.l2j.loginserver.network.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sf.l2j.commons.mmocore.ReceivablePacket;
import sf.l2j.loginserver.network.LoginClient;

public abstract class L2LoginClientPacket extends ReceivablePacket<LoginClient> {

	private static Logger _log = LoggerFactory.getLogger(L2LoginClientPacket.class.getName());

	@Override
	protected final boolean read() {
		try {
			return readImpl();
		} catch (Exception e) {
			_log.error("ERROR READING: " + this.getClass().getSimpleName());
			e.printStackTrace();
			return false;
		}
	}

	protected abstract boolean readImpl();
}
