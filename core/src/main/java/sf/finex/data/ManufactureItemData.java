package sf.finex.data;

import lombok.Data;
import sf.finex.data.tables.RecipeTable;

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
