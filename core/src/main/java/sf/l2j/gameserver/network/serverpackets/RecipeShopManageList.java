package sf.l2j.gameserver.network.serverpackets;

import java.util.Collection;
import java.util.Iterator;
import sf.finex.data.ManufactureItemData;
import sf.finex.data.RecipeData;
import sf.l2j.gameserver.model.L2ManufactureList;
import sf.l2j.gameserver.model.actor.Player;

/**
 * dd d(dd) d(ddd)
 */
public class RecipeShopManageList extends L2GameServerPacket {

	private final Player _seller;
	private final boolean _isDwarven;
	private final Collection<RecipeData> recipes;

	public RecipeShopManageList(Player seller, boolean isDwarven) {
		_seller = seller;
		_isDwarven = isDwarven;

		if (_isDwarven && seller.isCrafter()) {
			recipes = seller.getDwarvenRecipeBook();
		} else {
			recipes = seller.getCommonRecipeBook();
		}

		// clean previous recipes
		if (seller.getCreateList() != null) {
			final Iterator<ManufactureItemData> it = seller.getCreateList().getList().iterator();
			while (it.hasNext()) {
				ManufactureItemData item = it.next();
				if (item.isDwarven() != _isDwarven || !seller.hasRecipe(item.getCraftId())) {
					it.remove();
				}
			}
		}
	}

	@Override
	protected final void writeImpl() {
		writeC(0xd8);
		writeD(_seller.getObjectId());
		writeD(_seller.getAdena());
		writeD(_isDwarven ? 0x00 : 0x01);

		if (recipes == null) {
			writeD(0);
		} else {
			writeD(recipes.size());// number of items in recipe book

			int i = 0;
			for (RecipeData recipe : recipes) {
				writeD(recipe.getCraftId());
				writeD(++i);
			}
		}

		if (_seller.getCreateList() == null) {
			writeD(0);
		} else {
			L2ManufactureList list = _seller.getCreateList();
			writeD(list.size());

			for (ManufactureItemData item : list.getList()) {
				writeD(item.getCraftId());
				writeD(0x00);
				writeD(item.getPrice());
			}
		}
	}
}
