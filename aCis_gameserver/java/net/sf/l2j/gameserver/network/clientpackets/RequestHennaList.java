package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.tables.DyeTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.HennaEquipList;

public final class RequestHennaList extends L2GameClientPacket {

	@SuppressWarnings("unused")
	private int _unknown;

	@Override
	protected void readImpl() {
		_unknown = readD(); // ??
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		activeChar.sendPacket(new HennaEquipList(activeChar, DyeTable.getInstance().holder()));
	}
}
