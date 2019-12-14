package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class PledgeShowMemberListDeleteAll extends L2GameServerPacket {

	public static final PledgeShowMemberListDeleteAll STATIC_PACKET = new PledgeShowMemberListDeleteAll();

	private PledgeShowMemberListDeleteAll() {
	}

	@Override
	protected final void writeImpl() {
		writeC(0x82);
	}
}
