package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.RecipeData;
import net.sf.finex.data.tables.RecipeTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.finex.enums.EStoreType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.RecipeBookItemList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

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
