package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

/**
 * Format: ch (trigger)
 * @author KenM
 */
public class ExShowAdventurerGuideBook extends L2GameServerPacket
{
	public static final ExShowAdventurerGuideBook STATIC_PACKET = new ExShowAdventurerGuideBook();
	
	private ExShowAdventurerGuideBook()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x37);
	}
}