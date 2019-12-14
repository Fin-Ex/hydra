package net.sf.finex.data;

import org.slf4j.LoggerFactory;

import lombok.Data;
import net.sf.finex.data.tables.RecipeTable;

@Data
public class ManufactureItemData {

	private final int craftId;
	private final int price;
	private final boolean isDwarven;

	public ManufactureItemData(int recipeId, int cost) {
		craftId = recipeId;
		price = cost;
		isDwarven = RecipeTable.getInstance().get(craftId).isDwarven();
	}
}
