/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.finex.data.QuestRewardData;
import net.sf.finex.data.RandomQuestConditionData;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.model.generator.quest.RandomQuestManager;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.RaidBoss;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.kind.Item;

/**
 *
 * @author FinFan
 */
public class BossHuntBuilder extends RandomQuestBuilder {

	@Override
	public void buildCondition() {
		final int minLvl = quest.getGrade().getMinLevel();
		final int maxLvl = quest.getGrade().getMaxLevel();

		// set boss depends level between min and max
		NpcTemplate boss = null;
		final Map<Integer, List<NpcTemplate>> list = RandomQuestManager.getInstance().getBosses();
		while (boss == null) {
			final int value = Rnd.get(minLvl, maxLvl);
			if (list.containsKey(value)) {
				boss = Rnd.get(list.get(value));
			}
		}
		quest.setCondition(new RandomQuestConditionData(quest.getType(), boss, 0));
	}

	@Override
	public void buildRewards() {
		final List<QuestRewardData> rewards = new ArrayList<>();
		final EGradeType grade = quest.getGrade();
		// give slayer coins
		rewards.add(new QuestRewardData(Item.SLAYER_COIN, Rnd.get(grade.getMinLevel(), grade.getMaxLevel()), true));
		quest.setRewards(rewards);
	}

	@Override
	public void buildDescription() {
		final StringBuilder descr = new StringBuilder();
		final NpcTemplate template = (NpcTemplate) quest.getCondition().getTarget();

		String closestTownName = null;
		final RaidBoss boss = (RaidBoss) World.getInstance().getNpcTemplates().get(template);
		if (boss != null) {
			closestTownName = MapRegionTable.getInstance().getClosestTownName(boss.getX(), boss.getY());
		}

		descr.append("<center>Description</center><br>");
		descr.append("Critical task! On the outskirts of the <font color=LEVEL>").append(closestTownName).append("</font>, you must find and destroy the <font color=LEVEL>")
				.append(template.getName()).append("</font>.<br1>For this task, it is better to select a group, because alone - the chances of success are very small.");
		descr.append("<table width=272><tr>");
		descr.append("<td align=center><button value=\"Take\" action=\"bypass -h npc_%objectId%_takeQuest ").append(quest.getId()).append("\" width=74 height=24 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("<td align=center><button value=\"Cancel\" action=\"bypass -h npc_%objectId%_cancelQuest\" width=74 height=23 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("<td align=center><button value=\"Return\" action=\"bypass -h npc_%objectId%_showQuestList\" width=74 height=23 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("</tr></table>");
		quest.setDescription(descr.toString());
	}

	@Override
	public void buildName() {
		quest.setName(quest.getType().getQuestName() + ((NpcTemplate) quest.getCondition().getTarget()).getName());
	}

	@Override
	public void buildExpAndSp() {
	}

	@Override
	public void buildQuestItems() {
	}
}
