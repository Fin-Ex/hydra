package sf.l2j.gameserver.network.serverpackets;

public class StopRotation extends L2GameServerPacket {

	private final int _charObjId;
	private final int _degree;
	private final int _speed;

	public StopRotation(int objid, int degree, int speed) {
		_charObjId = objid;
		_degree = degree;
		_speed = speed;
	}

	public StopRotation(int objid, int degree) {
		_charObjId = objid;
		_degree = degree;
		_speed = Short.MAX_VALUE;
	}

	@Override
	protected final void writeImpl() {
		writeC(0x63);
		writeD(_charObjId);
		writeD(_degree);
		writeD(_speed);
		writeC(_degree);
	}
}