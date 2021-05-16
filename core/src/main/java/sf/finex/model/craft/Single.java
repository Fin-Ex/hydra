/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.craft;

import sf.finex.data.RecipeData;
import lombok.extern.slf4j.Slf4j;
import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.ItemList;
import sf.l2j.gameserver.network.serverpackets.RecipeItemMakeInfo;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Craft items by warsmiths (themselfs only).
 *
 * @author FinFan
 */
@Slf4j
public class Single extends Craft {

	public Single(Player crafter, RecipeData recipe) {
		super(crafter, recipe);
	}

	@Override
	public void create() {
		boolean success = false;

		// check item enought & reduce them if enought
		if (!validateCreating()) {
			crafter.sendPacket(ActionFailed.STATIC_PACKET);
		} // if items enought and adnea too
		else if (validateIngredients()) {
			crafter.reduceCurrentMp(recipe.getMpConsume());

			success = calculateResult();
			final boolean hasWsmComponent = warsmith != null;
			if (success) {
				reward();
				if (hasWsmComponent) {
					warsmith.addExp(recipe);
				}
			}
		}

		update(success);
		crafter.setCraft(null);
	}

	@Override
	protected void update(boolean result) {
		crafter.sendPacket(new ItemList(crafter, false));
		crafter.sendPacket(new RecipeItemMakeInfo(recipe.getCraftId(), crafter, result ? 1 : 0));
		updateStatus();
	}

	@Override
	protected boolean calculateResult() {
		return Rnd.get(100) < recipe.getSuccessRate();
	}

	@Override
	protected void reward() {
		final int createId = recipe.getProduct().getId();
		final int createCount = recipe.getProduct().getValue();

		crafter.getInventory().addItem("CreateItem", createId, createCount, crafter, crafter);

		if (createCount > 1) {
			crafter.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(createId).addNumber(createCount));
		} else {
			crafter.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(createId));
		}
	}
}
