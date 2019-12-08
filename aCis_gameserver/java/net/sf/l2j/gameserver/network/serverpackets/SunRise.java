package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class SunRise extends L2GameServerPacket
{
	public static final SunRise STATIC_PACKET = new SunRise();
	
	private SunRise()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x1c);
	}
}
