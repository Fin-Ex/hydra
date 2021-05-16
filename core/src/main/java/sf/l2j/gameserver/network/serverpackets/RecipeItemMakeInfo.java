package sf.l2j.gameserver.network.serverpackets;

import sf.finex.data.RecipeData;
import sf.finex.data.tables.RecipeTable;
import sf.l2j.gameserver.model.actor.Player;

/**
 * format dddd
 */
public class RecipeItemMakeInfo extends L2GameServerPacket {

	private final int _id;
	private final Player _activeChar;
	private final int _status;

	public RecipeItemMakeInfo(int id, Player player, int status) {
		_id = id;
		_activeChar = player;
		_status = status;
	}

	public RecipeItemMakeInfo(int id, Player player) {
		_id = id;
		_activeChar = player;
		_status = -1;
	}

	@Override
	protected final void writeImpl() {
		RecipeData recipe = RecipeTable.getInstance().get(_id);
		if (recipe != null) {
			writeC(0xD7);

			writeD(_id);
			writeD(recipe.isDwarven() ? 0 : 1);
			writeD((int) _activeChar.getCurrentMp());
			writeD(_activeChar.getMaxMp());
			writeD(_status);
		}
	}
}
