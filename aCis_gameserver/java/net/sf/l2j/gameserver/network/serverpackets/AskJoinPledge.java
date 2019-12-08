package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class AskJoinPledge extends L2GameServerPacket
{
	private final int _requestorObjId;
	private final String _pledgeName;
	
	public AskJoinPledge(int requestorObjId, String pledgeName)
	{
		_requestorObjId = requestorObjId;
		_pledgeName = pledgeName;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x32);
		writeD(_requestorObjId);
		writeS(_pledgeName);
	}
}