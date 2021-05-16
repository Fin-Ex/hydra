package sf.l2j.gameserver.handler.itemhandlers;

import sf.l2j.gameserver.handler.IHandler;

import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ChooseInventoryItem;

public class EnchantScrolls implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!(playable instanceof Player)) {
			return;
		}

		final Player activeChar = (Player) playable;
		if (activeChar.isCastingNow()) {
			return;
		}

		if (activeChar.getActiveEnchantItem() == null) {
			activeChar.sendPacket(SystemMessageId.SELECT_ITEM_TO_ENCHANT);
		}

		activeChar.setActiveEnchantItem(item);
		activeChar.sendPacket(new ChooseInventoryItem(item.getItemId()));
	}
}