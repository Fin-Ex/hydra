package sf.l2j.gameserver.network.clientpackets;

import sf.finex.data.RecipeData;
import sf.finex.data.tables.RecipeTable;
import sf.l2j.gameserver.model.actor.Player;
import sf.finex.enums.EStoreType;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.RecipeBookItemList;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestRecipeBookDestroy extends L2GameClientPacket {

	private int _recipeID;

	@Override
	protected void readImpl() {
		_recipeID = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.getStoreType() == EStoreType.MANUFACTURE) {
			activeChar.sendPacket(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
			return;
		}

		final RecipeData recipeData = RecipeTable.getInstance().get(_recipeID);
		if (recipeData == null) {
			return;
		}

		activeChar.unregisterRecipeList(_recipeID);
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED).addItemName(_recipeID));

		RecipeBookItemList response = new RecipeBookItemList(recipeData.isDwarven(), activeChar.getMaxMp());
		if (recipeData.isDwarven()) {
			response.addRecipes(activeChar.getDwarvenRecipeBook());
		} else {
			response.addRecipes(activeChar.getCommonRecipeBook());
		}

		activeChar.sendPacket(response);
	}
}
