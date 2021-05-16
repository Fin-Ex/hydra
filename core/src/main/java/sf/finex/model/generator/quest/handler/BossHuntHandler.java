/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.generator.quest.handler;

import sf.finex.data.RandomQuestData;
import sf.finex.model.generator.quest.RandomQuestComponent;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.events.OnKill;
import sf.l2j.gameserver.model.actor.instance.RaidBoss;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 *
 * @author FinFan
 */
public class BossHuntHandler extends RandomQuestHandler {

	public BossHuntHandler() {
		eventBus.subscribe().cast(OnKill.class).forEach(this::onKill);
	}

	private void onKill(OnKill event) {
		if (event.getKiller() == null || !event.getKiller().isPlayer()) {
			return;
		}

		final Player player = event.getKiller().getPlayer();
		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		if (!component.hasQuest()) {
			return;
		}

		final RandomQuestData quest = component.getQuest();
		if (quest.isDone()) {
			return;
		}

		final NpcTemplate targetTemplate = (NpcTemplate) quest.getCondition().getTarget();
		final RaidBoss boss = (RaidBoss) event.getVictim();
		if (boss.getNpcId() == targetTemplate.getNpcId()) {
			quest.setDone(true);
		}

		player.sendMessage("Quest finished! Return to to Quest Board and talk with them!");
	}
}
