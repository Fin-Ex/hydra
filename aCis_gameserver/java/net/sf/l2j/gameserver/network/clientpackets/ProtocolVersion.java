package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.network.serverpackets.KeyPacket;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

public final class ProtocolVersion extends L2GameClientPacket {

	private int _version;

	@Override
	protected void readImpl() {
		_version = readD();
	}

	@Override
	protected void runImpl() {
		if (_version == -2) {
			getClient().close((L2GameServerPacket) null);
		} else if (_version < Config.MIN_PROTOCOL_REVISION || _version > Config.MAX_PROTOCOL_REVISION) {
			_log.warn("Client: " + getClient().toString() + " -> Protocol Revision: " + _version + " is invalid. Minimum and maximum allowed are: " + Config.MIN_PROTOCOL_REVISION + " and " + Config.MAX_PROTOCOL_REVISION + ". Closing connection.");
			getClient().close((L2GameServerPacket) null);
		} else {
			getClient().sendPacket(new KeyPacket(getClient().enableCrypt()));
		}
	}
}
