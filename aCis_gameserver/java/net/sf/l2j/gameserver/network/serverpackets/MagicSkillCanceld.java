package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class MagicSkillCanceld extends L2GameServerPacket
{
	private final int _objectId;
	
	public MagicSkillCanceld(int objectId)
	{
		_objectId = objectId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x49);
		writeD(_objectId);
	}
}