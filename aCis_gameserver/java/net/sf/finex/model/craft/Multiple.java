/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.craft;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.ManufactureItemData;
import net.sf.finex.data.RecipeData;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.RecipeShopItemInfo;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Craft items by CLIENTS only using the warsmith Manufacture Tool.
 *
 * @author FinFan
 */
@Slf4j
public class Multiple extends Craft {

	private int price;

	public Multiple(Player crafter, Player client, RecipeData recipe) {
		super(crafter, client, recipe);

		// get the item craetion price
		for (ManufactureItemData temp : crafter.getCreateList().getList()) {
			if (temp.getCraftId() == recipe.getCraftId()) {
				price = temp.getPrice();
			}
		}
	}

	/* IMPLEMENTATION *************************************************/
	@Override
	public void create() {
		boolean success = false;

		// check adena in inventory
		if (price > client.getAdena()) {
			client.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
		} // check item enought & reduce them if enought
		else if (!validateCreating()) {
			client.sendPacket(ActionFailed.STATIC_PACKET);
		} // if items enought and adnea too
		else if (validateIngredients()) {
			if (price > 0) {
				client.transferItem("PayManufacture", client.getInventory().getAdenaInstance().getObjectId(), price, crafter.getInventory(), crafter);
			}

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
		client.setCraft(null);
	}

	@Override
	protected void update(boolean result) {
		client.sendPacket(new ItemList(client, false));
		client.sendPacket(new RecipeShopItemInfo(crafter, recipe.getCraftId()));
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

		client.getInventory().addItem("Manufacture", createId, createCount, client, crafter);

		if (createCount == 1) {
			crafter.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_CREATED_FOR_S1_FOR_S3_ADENA).addString(client.getName()).addItemName(createId).addItemNumber(price));
			client.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CREATED_S2_FOR_S3_ADENA).addString(crafter.getName()).addItemName(createId).addItemNumber(price));
		} else {
			crafter.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S3_S_CREATED_FOR_S1_FOR_S4_ADENA).addString(client.getName()).addNumber(createCount).addItemName(createId).addItemNumber(price));
			client.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CREATED_S2_S3_S_FOR_S4_ADENA).addString(crafter.getName()).addNumber(createCount).addItemName(createId).addItemNumber(price));
		}
	}

	@Override
	protected boolean validateCreating() {
		if (!super.validateCreating()) {
			return false;
		}

		if (!crafter.isOnline()) {
			return false;
		}

		if (crafter.getCurrentMp() < recipe.getMpConsume()) {
			client.sendPacket(SystemMessageId.NOT_ENOUGH_MP);
			return false;
		}

		return true;
	}
}
