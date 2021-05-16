package sf.l2j.gameserver.handler.itemhandlers;

import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Elixir extends ItemSkills {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!playable.isPlayer()) {
			playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ITEM_NOT_FOR_PETS));
			return;
		}
		super.invoke(playable, item, forceUse);
	}
}
