package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

/**
 * format cdd
 */
public class FriendAddRequest extends L2GameServerPacket
{
	private final String _requestorName;
	
	public FriendAddRequest(String requestorName)
	{
		_requestorName = requestorName;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7d);
		writeS(_requestorName);
		writeD(0);
	}
}