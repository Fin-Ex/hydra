package sf.l2j.gameserver.network.clientpackets;

import sf.finex.data.tables.RecipeTable;
import sf.finex.model.craft.Craft;
import sf.finex.model.craft.Single;
import sf.l2j.gameserver.model.actor.Player;
import sf.finex.enums.EStoreType;
import sf.l2j.gameserver.network.FloodProtectors;
import sf.l2j.gameserver.network.FloodProtectors.Action;

public final class RequestRecipeItemMakeSelf extends L2GameClientPacket {

	private int _id;

	@Override
	protected void readImpl() {
		_id = readD();
	}

	@Override
	protected void runImpl() {
		if (!FloodProtectors.performAction(getClient(), Action.MANUFACTURE)) {
			return;
		}

		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.getStoreType() == EStoreType.MANUFACTURE || activeChar.isCrafting()) {
			return;
		}

		final Craft craft = new Single(activeChar, RecipeTable.getInstance().get(_id));
		activeChar.setCraft(craft);
		craft.create();
	}
}
