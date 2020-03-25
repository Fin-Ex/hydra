package net.sf.l2j.gameserver.network.serverpackets;

public class StartRotation extends L2GameServerPacket {

	private final int _charObjId, _degree, _side, _speed;

	public StartRotation(int objId, int degree, int side, int speed) {
		_charObjId = objId;
		_degree = degree;
		_side = side;
		_speed = speed;
	}

	/**
	 * Specified constructor with: <b>Side</b> = 1 and <b>Speed</b> is {@link Short.MAX_VALUE}
	 * @param objId
	 * @param degree 
	 */
	public StartRotation(int objId, int degree) {
		_charObjId = objId;
		_degree = degree;
		_side = 1;
		_speed = Short.MAX_VALUE;
	}

	@Override
	protected final void writeImpl() {
		writeC(0x62);
		writeD(_charObjId);
		writeD(_degree);
		writeD(_side);
		writeD(_speed);
	}
}
