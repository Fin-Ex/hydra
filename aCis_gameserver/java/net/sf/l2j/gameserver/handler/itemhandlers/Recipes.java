package net.sf.l2j.gameserver.handler.itemhandlers;


import net.sf.finex.model.classes.Warsmith;
import net.sf.finex.data.RecipeData;
import net.sf.finex.data.tables.RecipeTable;
import net.sf.finex.data.SpecializeData;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.finex.enums.EStoreType;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Recipes implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!playable.isPlayer() || !playable.hasComponent(Warsmith.class)) {
			return;
		}

		final Player activeChar = (Player) playable;

		if (activeChar.isCrafting()) {
			activeChar.sendPacket(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
			return;
		}

		final RecipeData recipeData = RecipeTable.getInstance().get(item.getItemId(), true);
		if (recipeData == null) {
			return;
		}

		if (activeChar.hasRecipe(recipeData.getCraftId())) {
			activeChar.sendPacket(SystemMessageId.RECIPE_ALREADY_REGISTERED);
			return;
		}

		if (recipeData.isDwarven()) {
			if (activeChar.hasComponent(Warsmith.class)) {
				final Warsmith warsmith = activeChar.getComponent(Warsmith.class);
				final SpecializeData specData = warsmith.getSpecialize(recipeData.getSpec());
				if (activeChar.getStoreType() == EStoreType.MANUFACTURE) {
					activeChar.sendPacket(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
				} else if (recipeData.getLevel() > specData.getLvl()) {
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LVL_TOO_LOW_TO_REGISTER).addString(specData.getSpecialize().getName()));
				} else if (activeChar.getDwarvenRecipeBook().size() >= activeChar.getDwarfRecipeLimit()) {
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.UP_TO_S1_RECIPES_CAN_REGISTER).addNumber(activeChar.getDwarfRecipeLimit()));
				} else {
					activeChar.registerDwarvenRecipeList(recipeData);
					activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ADDED).addItemName(item));
				}
			} else {
				activeChar.sendPacket(SystemMessageId.CANT_REGISTER_NO_ABILITY_TO_CRAFT);
			}
		} else {
			if (activeChar.hasCommonCraft()) {
				if (activeChar.getStoreType() == EStoreType.MANUFACTURE) {
					activeChar.sendPacket(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
				} else if (recipeData.getLevel() > activeChar.getCommonCraft()) {
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LVL_TOO_LOW_TO_REGISTER).addString("Common Specialize"));
				} else if (activeChar.getCommonRecipeBook().size() >= activeChar.getCommonRecipeLimit()) {
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.UP_TO_S1_RECIPES_CAN_REGISTER).addNumber(activeChar.getCommonRecipeLimit()));
				} else {
					activeChar.registerCommonRecipeList(recipeData);
					activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ADDED).addItemName(item));
				}
			} else {
				activeChar.sendPacket(SystemMessageId.CANT_REGISTER_NO_ABILITY_TO_CRAFT);
			}
		}
	}
}
