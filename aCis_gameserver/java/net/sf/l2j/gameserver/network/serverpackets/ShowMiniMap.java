package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.instancemanager.SevenSigns;

public class ShowMiniMap extends L2GameServerPacket
{
	public static final ShowMiniMap REGULAR_MAP = new ShowMiniMap(1665);
	
	private final int _mapId;
	
	public ShowMiniMap(int mapId)
	{
		_mapId = mapId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9d);
		writeD(_mapId);
		writeD(SevenSigns.getInstance().getCurrentPeriod().ordinal());
	}
}