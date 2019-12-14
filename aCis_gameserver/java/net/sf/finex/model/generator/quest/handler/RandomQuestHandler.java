/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.handler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.RandomQuestData;
import net.sf.finex.events.EventBus;
import net.sf.finex.model.generator.quest.RandomQuestComponent;
import net.sf.finex.model.generator.quest.RandomQuestManager;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnTalk;

/**
 *
 * @author FinFan
 */
@Slf4j
public abstract class RandomQuestHandler {

	@Getter
	protected final EventBus eventBus = new EventBus();

	public RandomQuestHandler() {
		eventBus.subscribe().cast(OnTalk.class).forEach(this::onTalk);
	}

	public boolean onGetQuest(RandomQuestData quest, Player player, Npc npc) {
		return true;
	}

	protected boolean onPassQuest(Player player, Npc npc) {
		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		if (!component.hasQuest()) {
			return false;
		}

		final RandomQuestData quest = component.getQuest();
		if (!quest.isDone()) {
			log.info("{} is not done!", quest.getName());
			return false;
		}

		return quest.getBoardId() == npc.getNpcId();
	}

	protected void onTalk(OnTalk event) {
		if (onPassQuest(event.getTalker(), event.getTarget())) {
			RandomQuestManager.getInstance().completeQuest(event.getTalker(), event.getTarget());
		}
	}
}
