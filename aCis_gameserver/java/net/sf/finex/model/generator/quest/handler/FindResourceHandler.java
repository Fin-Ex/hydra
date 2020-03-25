/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.handler;

import net.sf.finex.data.RandomQuestData;
import net.sf.finex.model.generator.quest.RandomQuestComponent;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;

/**
 *
 * @author FinFan
 */
public class FindResourceHandler extends RandomQuestHandler {

	public FindResourceHandler() {
	}

	@Override
	protected boolean onPassQuest(Player player, Npc npc) {
		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		final RandomQuestData quest = component.getQuest();
		if (quest == null) {
			return false;
		}

		final Item condItem = (Item) quest.getCondition().getTarget();
		final ItemInstance resource = player.getInventory().getItemByItemId(condItem.getItemId());
		if (resource == null) {
			return false;
		}

		return player.destroyItem("FindResourceItemPass", resource, quest.getCondition().getValue(), null, true);
	}
}
