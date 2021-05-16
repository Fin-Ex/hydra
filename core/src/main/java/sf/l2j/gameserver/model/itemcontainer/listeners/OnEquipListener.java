package sf.l2j.gameserver.model.itemcontainer.listeners;

import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;

public interface OnEquipListener {

	public void onEquip(int slot, ItemInstance item, Playable actor);

	public void onUnequip(int slot, ItemInstance item, Playable actor);
}
