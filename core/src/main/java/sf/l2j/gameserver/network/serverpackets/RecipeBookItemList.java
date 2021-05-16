package sf.l2j.gameserver.network.serverpackets;

import java.util.Collection;
import sf.finex.data.RecipeData;

/**
 * format d d(dd)
 */
public class RecipeBookItemList extends L2GameServerPacket {

	private Collection<RecipeData> _recipes;
	private final boolean _isDwarvenCraft;
	private final int _maxMp;

	public RecipeBookItemList(boolean isDwarvenCraft, int maxMp) {
		_isDwarvenCraft = isDwarvenCraft;
		_maxMp = maxMp;
	}

	public void addRecipes(Collection<RecipeData> recipeBook) {
		_recipes = recipeBook;
	}

	@Override
	protected final void writeImpl() {
		writeC(0xD6);

		writeD(_isDwarvenCraft ? 0x00 : 0x01); // 0 = Dwarven - 1 = Common
		writeD(_maxMp);

		if (_recipes == null) {
			writeD(0);
		} else {
			writeD(_recipes.size());// number of items in recipe book

			int i = 0;
			for (RecipeData recipe : _recipes) {
				writeD(recipe.getCraftId());
				writeD(++i);
			}
		}
	}
}
