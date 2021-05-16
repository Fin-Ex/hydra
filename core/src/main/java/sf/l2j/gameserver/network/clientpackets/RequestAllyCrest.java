package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.cache.CrestCache;
import sf.l2j.gameserver.cache.CrestCache.CrestType;
import sf.l2j.gameserver.network.serverpackets.AllyCrest;

public final class RequestAllyCrest extends L2GameClientPacket {

	private int _crestId;

	@Override
	protected void readImpl() {
		_crestId = readD();
	}

	@Override
	protected void runImpl() {
		byte[] data = CrestCache.getInstance().getCrest(CrestType.ALLY, _crestId);
		if (data != null) {
			sendPacket(new AllyCrest(_crestId, data));
		}
	}
}
