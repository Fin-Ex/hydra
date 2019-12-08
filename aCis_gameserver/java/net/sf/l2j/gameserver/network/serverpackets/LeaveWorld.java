package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class LeaveWorld extends L2GameServerPacket
{
	public static final LeaveWorld STATIC_PACKET = new LeaveWorld();
	
	private LeaveWorld()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7e);
	}
}