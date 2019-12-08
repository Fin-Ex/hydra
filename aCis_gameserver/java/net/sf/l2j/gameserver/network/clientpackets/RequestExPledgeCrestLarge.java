package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.cache.CrestCache.CrestType;
import net.sf.l2j.gameserver.network.serverpackets.ExPledgeCrestLarge;

/**
 * Fomat : chd
 * @author -Wooden-
 */
public final class RequestExPledgeCrestLarge extends L2GameClientPacket
{
	private int _crestId;
	
	@Override
	protected void readImpl()
	{
		_crestId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		byte[] data = CrestCache.getInstance().getCrest(CrestType.PLEDGE_LARGE, _crestId);
		if (data != null)
			sendPacket(new ExPledgeCrestLarge(_crestId, data));
	}
}