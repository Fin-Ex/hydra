package sf.l2j.gameserver.handler.itemhandlers;

import sf.l2j.gameserver.handler.IHandler;

import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.network.serverpackets.ShowXMasSeal;

public class SpecialXMas implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!(playable instanceof Player)) {
			return;
		}

		playable.sendPacket(new ShowXMasSeal(item.getItemId()));
	}
}