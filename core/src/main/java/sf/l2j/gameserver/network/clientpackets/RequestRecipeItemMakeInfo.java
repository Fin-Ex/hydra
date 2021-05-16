package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.RecipeItemMakeInfo;

public final class RequestRecipeItemMakeInfo extends L2GameClientPacket {

	private int _id;

	@Override
	protected void readImpl() {
		_id = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		activeChar.sendPacket(new RecipeItemMakeInfo(_id, activeChar));
	}
}
