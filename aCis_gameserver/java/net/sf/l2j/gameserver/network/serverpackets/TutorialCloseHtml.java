package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class TutorialCloseHtml extends L2GameServerPacket
{
	public static final TutorialCloseHtml STATIC_PACKET = new TutorialCloseHtml();
	
	private TutorialCloseHtml()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xa3);
	}
}