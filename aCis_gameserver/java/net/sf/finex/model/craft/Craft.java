/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.craft;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.SpecializeData;
import net.sf.finex.data.RecipeData;
import java.util.ArrayList;
import java.util.List;
import net.sf.finex.model.classes.Warsmith;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author FinFan
 */
public abstract class Craft {

	/* GLOBAL *************************************/
	private static final int[] EXP = {
		0, // 1 level
		100,
		300,
		900,
		2700,
		8100,
		24300,
		72900,
		218700 // 9 level
	};

	public static final int MAX_LVL = EXP.length; // 9 level is max

	public static final int getExpForLevel(int lvl) {
		if (lvl >= EXP.length) {
			return EXP[EXP.length - 1]; // max
		}

		if (lvl < 0) {
			return EXP[1]; //min
		}

		return EXP[lvl];
	}

	/* CLASS METHODS *************************************/
	protected final Warsmith warsmith;
	protected final Player crafter, client;
	protected final RecipeData recipe;

	public Craft(Player crafter, Player client, RecipeData recipe) {
		this.crafter = crafter;
		this.client = client;
		this.recipe = recipe;
		this.warsmith = crafter.getComponent(Warsmith.class);
	}

	public Craft(Player crafter, RecipeData recipe) {
		this(crafter, crafter, recipe);
	}

	public abstract void create();

	public Player getCrafter() {
		return crafter;
	}

	public RecipeData getRecipe() {
		return recipe;
	}

	protected boolean checkResources() {
		return true;
	}

	protected abstract void update(boolean result);

	protected abstract boolean calculateResult();

	protected void updateStatus() {
		final StatusUpdate su = new StatusUpdate(client);
		su.addAttribute(StatusUpdate.CUR_MP, (int) client.getCurrentMp());
		su.addAttribute(StatusUpdate.CUR_LOAD, client.getCurrentLoad());
		client.sendPacket(su);
	}

	protected abstract void reward();

	protected boolean validateCreating() {
		if (client.isAlikeDead()) {
			return false;
		}

		if (client.isProcessingTransaction()) {
			return false;
		}

		// validate recipe list
		if (recipe.getIngredients().isEmpty()) {
			return false;
		}

		// validate level
		final int skillLevel;
		if (warsmith != null) {
			final SpecializeData specData = warsmith.getSpecialize(recipe.getSpec());
			skillLevel = specData.getLvl();
		} else {
			skillLevel = L2Skill.SKILL_CREATE_COMMON;
		}

		return recipe.getLevel() <= skillLevel;
	}

	protected boolean validateIngredients() {
		final Inventory inv = client.getInventory();
		final List<IntIntHolder> materials = new ArrayList<>();

		boolean gotAllMats = true;
		for (IntIntHolder neededPart : recipe.getIngredients()) {
			final Item item = ItemTable.getInstance().getTemplate(recipe.getProduct().getId());
			final int quantity = item.isConsumable() ? (int) (neededPart.getValue() * Config.RATE_CONSUMABLE_COST) : (int) neededPart.getValue();
			if (quantity > 0) {
				final ItemInstance instance = inv.getItemByItemId(neededPart.getId());
				if (instance == null || instance.getCount() < quantity) {
					client.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.MISSING_S2_S1_TO_CREATE).addItemName(neededPart.getId()).addItemNumber((instance == null) ? quantity : quantity - instance.getCount()));
					gotAllMats = false;
				} else {
					materials.add(new IntIntHolder(instance.getItemId(), quantity));
				}
			}
		}

		if (!gotAllMats) {
			return false;
		}

		for (IntIntHolder material : materials) {
			inv.destroyItemByItemId("Manufacture", material.getId(), material.getValue(), client, crafter);
			if (material.getValue() > 1) {
				client.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED).addItemName(material.getId()).addItemNumber(material.getValue()));
			} else {
				client.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED).addItemName(material.getId()));
			}
		}

		return true;
	}
}
