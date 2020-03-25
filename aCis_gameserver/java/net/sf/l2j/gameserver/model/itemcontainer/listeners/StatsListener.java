package net.sf.l2j.gameserver.model.itemcontainer.listeners;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;

public class StatsListener implements OnEquipListener {

	private static StatsListener instance = new StatsListener();

	public static StatsListener getInstance() {
		return instance;
	}

	@Override
	public void onEquip(int slot, ItemInstance item, Playable playable) {
		playable.addStatFuncs(item.getStatFuncs(playable));
	}

	@Override
	public void onUnequip(int slot, ItemInstance item, Playable playable) {
		playable.removeStatsByOwner(item);
	}
}
