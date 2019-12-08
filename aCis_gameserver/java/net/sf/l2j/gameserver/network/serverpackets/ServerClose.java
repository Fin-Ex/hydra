package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

/**
 * @author devScarlet & mrTJO
 */
public class ServerClose extends L2GameServerPacket
{
	public static final ServerClose STATIC_PACKET = new ServerClose();
	
	private ServerClose()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x26);
	}
}