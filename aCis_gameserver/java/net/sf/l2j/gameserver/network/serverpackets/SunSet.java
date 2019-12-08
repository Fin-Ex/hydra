package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class SunSet extends L2GameServerPacket
{
	public static final SunSet STATIC_PACKET = new SunSet();
	
	private SunSet()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x1d);
	}
}