package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.ItemList;

public final class RequestItemList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (!activeChar.isInventoryDisabled()) {
			sendPacket(new ItemList(activeChar, true));
		}
	}
}
