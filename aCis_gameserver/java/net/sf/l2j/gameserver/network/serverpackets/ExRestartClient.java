package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

/**
 * Format: (ch)
 * @author -Wooden-
 */
public class ExRestartClient extends L2GameServerPacket
{
	public static final ExRestartClient STATIC_PACKET = new ExRestartClient();
	
	private ExRestartClient()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x47);
	}
}