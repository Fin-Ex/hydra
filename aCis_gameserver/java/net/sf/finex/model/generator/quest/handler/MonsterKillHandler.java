/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.handler;

import net.sf.finex.data.RandomQuestData;
import net.sf.finex.model.generator.quest.RandomQuestComponent;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnKill;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 *
 * @author FinFan
 */
public class MonsterKillHandler extends RandomQuestHandler {

	public MonsterKillHandler() {
		eventBus.subscribe().cast(OnKill.class).forEach(this::onKill);
	}
	
	// handle notification
	private void onKill(OnKill event) {
		if(event.getKiller() == null || !event.getKiller().isPlayer()) {
			return;
		}
		
		final Player player = event.getKiller().getPlayer();
		final Monster target = (Monster) event.getVictim();
		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		if(!component.hasQuest()) {
			return;
		}
		
		final RandomQuestData quest = component.getQuest();
		if(quest.isDone()) {
			return;
		}
		
		final NpcTemplate targetTemplate = (NpcTemplate) quest.getCondition().getTarget();
		if(target.getNpcId() == targetTemplate.getNpcId()) {
			quest.setCounter(quest.getCounter() + 1);
		}
		
		player.sendMessage("Progress: " + String.format("%1.2f", quest.getCounter() * 1. / quest.getCondition().getValue() * 100.) + "%.");
		
		// check if complete
		if(quest.getCounter() >= quest.getCondition().getValue()) {
			quest.setDone(true);
			player.sendMessage("Quest finished! Return to to Quest Board and talk with them!");
		}
	}
}
