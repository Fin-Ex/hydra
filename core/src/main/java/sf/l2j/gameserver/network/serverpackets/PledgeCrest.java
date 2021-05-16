package sf.l2j.gameserver.network.serverpackets;

import sf.l2j.gameserver.cache.CrestCache;
import sf.l2j.gameserver.cache.CrestCache.CrestType;

public class PledgeCrest extends L2GameServerPacket {

	private final int _crestId;
	private final byte[] _data;

	public PledgeCrest(int crestId) {
		_crestId = crestId;
		_data = CrestCache.getInstance().getCrest(CrestType.PLEDGE, _crestId);
	}

	@Override
	protected final void writeImpl() {
		writeC(0x6c);
		writeD(_crestId);
		if (_data != null) {
			writeD(_data.length);
			writeB(_data);
		} else {
			writeD(0);
		}
	}
}
