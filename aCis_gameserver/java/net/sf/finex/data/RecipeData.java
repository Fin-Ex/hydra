/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import org.slf4j.LoggerFactory;

import net.sf.finex.enums.ECraftSpec;
import java.util.List;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.kind.Item;

/**
 *
 * @author FinFan
 */
public class RecipeData {

	private final int craftId;
	private final String name;
	private final int recipeItemId;
	private final int level;
	private final boolean isDwarven;

	private final int mpConsume;
	private final int successRate;
	private final IntIntHolder product;
	private final List<IntIntHolder> ingredients;

	public RecipeData(int craftId, String name, int recipeItemId, int level, boolean isDwarven, int mpConsume, int successRate, IntIntHolder product, List<IntIntHolder> ingredients) {
		this.craftId = craftId;
		this.name = name;
		this.recipeItemId = recipeItemId;
		this.level = level;
		this.isDwarven = isDwarven;
		this.mpConsume = mpConsume;
		this.successRate = successRate;
		this.product = product;
		this.ingredients = ingredients;
	}

	public int getCraftId() {
		return craftId;
	}

	public boolean isDwarven() {
		return isDwarven;
	}

	public String getName() {
		return name;
	}

	public List<IntIntHolder> getIngredients() {
		return ingredients;
	}

	public int getLevel() {
		return level;
	}

	public int getMpConsume() {
		return mpConsume;
	}

	public IntIntHolder getProduct() {
		return product;
	}

	public int getRecipeItemId() {
		return recipeItemId;
	}

	public int getSuccessRate() {
		return successRate;
	}

	public ECraftSpec getSpec() {
		final Item item = ItemTable.getInstance().getTemplate(product.getId());
		for (ECraftSpec next : ECraftSpec.values()) {
			if (next.checkItem(item.getClass())) {
				return next;
			}
		}

		return null;
	}
}
