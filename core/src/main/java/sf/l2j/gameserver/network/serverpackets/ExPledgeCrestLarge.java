package sf.l2j.gameserver.network.serverpackets;

/**
 * Format: (ch) ddd b
 *
 * @author -Wooden-
 */
public class ExPledgeCrestLarge extends L2GameServerPacket {

	private final int _crestId;
	private final byte[] _data;

	public ExPledgeCrestLarge(int crestId, byte[] data) {
		_crestId = crestId;
		_data = data;
	}

	@Override
	protected void writeImpl() {
		writeC(0xfe);
		writeH(0x28);

		writeD(0x00); // ???
		writeD(_crestId);
		writeD(_data.length);

		writeB(_data);
	}
}
