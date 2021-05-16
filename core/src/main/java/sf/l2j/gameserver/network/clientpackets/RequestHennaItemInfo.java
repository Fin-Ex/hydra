package sf.l2j.gameserver.network.clientpackets;

import sf.finex.data.tables.DyeTable;
import sf.finex.data.DyeData;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.HennaItemInfo;

public final class RequestHennaItemInfo extends L2GameClientPacket {

	private int _symbolId;

	@Override
	protected void readImpl() {
		_symbolId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final DyeData template = DyeTable.getInstance().get(_symbolId);
		if (template == null) {
			return;
		}

		activeChar.sendPacket(new HennaItemInfo(template, activeChar));
	}
}
