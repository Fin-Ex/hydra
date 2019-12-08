package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class AutoAttackStop extends L2GameServerPacket
{
	private final int _targetObjId;
	
	public AutoAttackStop(int targetObjId)
	{
		_targetObjId = targetObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x2c);
		writeD(_targetObjId);
	}
}