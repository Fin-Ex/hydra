package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class JoinPledge extends L2GameServerPacket {

	private final int _pledgeId;

	public JoinPledge(int pledgeId) {
		_pledgeId = pledgeId;
	}

	@Override
	protected final void writeImpl() {
		writeC(0x33);
		writeD(_pledgeId);
	}
}
