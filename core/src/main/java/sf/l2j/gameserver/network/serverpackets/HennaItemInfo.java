package sf.l2j.gameserver.network.serverpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.finex.data.DyeData;

public class HennaItemInfo extends L2GameServerPacket {

	private final Player _activeChar;
	private final DyeData _henna;

	public HennaItemInfo(DyeData henna, Player player) {
		_henna = henna;
		_activeChar = player;
	}

	@Override
	protected final void writeImpl() {
		writeC(0xe3);
		writeD(_henna.getSymbolId()); // symbol Id
		writeD(_henna.getDyeId()); // item id of dye
		writeD(DyeData.getRequiredDyeAmount()); // total amount of dye required
		writeD(_henna.getPrice()); // total amount of adenas required to draw symbol
		writeD(1); // able to draw or not 0 is false and 1 is true
		writeD(_activeChar.getAdena());

		writeD(_activeChar.getINT()); // current INT
		writeC(_activeChar.getINT() + _henna.getINT()); // equip INT
		writeD(_activeChar.getSTR()); // current STR
		writeC(_activeChar.getSTR() + _henna.getSTR()); // equip STR
		writeD(_activeChar.getCON()); // current CON
		writeC(_activeChar.getCON() + _henna.getCON()); // equip CON
		writeD(_activeChar.getMEN()); // current MEM
		writeC(_activeChar.getMEN() + _henna.getMEN()); // equip MEM
		writeD(_activeChar.getDEX()); // current DEX
		writeC(_activeChar.getDEX() + _henna.getDEX()); // equip DEX
		writeD(_activeChar.getWIT()); // current WIT
		writeC(_activeChar.getWIT() + _henna.getWIT()); // equip WIT
	}
}
