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
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.DropCategory;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author FinFan
 */
public class MonsterKillBuild extends RandomQuestBuilder {

	@Override
	public void buildCondition() {
		final Map<Integer, List<NpcTemplate>> monstersMap = RandomQuestManager.getInstance().getMonsters();
		final NpcTemplate monsterForHunt = Rnd.get(monstersMap.entrySet().stream()
				.filter(e -> e.getKey() >= quest.getGrade().getMinLevel() && e.getKey() <= quest.getGrade().getMaxLevel())
				.flatMap(e -> e.getValue().stream())
				.toArray(NpcTemplate[]::new)
		);
		
		final double modifier = (100.0 - 11 + monsterForHunt.getLevel()) / 100.0;
		final int monsterKillCount = (int) (Rnd.get(18, 72) / Math.max(modifier, 1.0));
		quest.setCondition(new RandomQuestConditionData(quest.getType(), monsterForHunt, monsterKillCount));
	}

	@Override
	public void buildRewards() {
		final List<QuestRewardData> list = new ArrayList<>();
		final NpcTemplate monster = (NpcTemplate) quest.getCondition().getTarget();
		if (monster == null) {
			return;
		}

		for (DropCategory cat : monster.getDropData()) {
			if (cat.isSweep()) {
				cat.getAllDrops().forEach(drop -> list.add(new QuestRewardData(drop.getItemId(), (int) Math.sqrt(Rnd.get(drop.getMinDrop(), drop.getMaxDrop()) * quest.getCondition().getCount()), true)));
			}
		}
		
		quest.setRewards(list);
	}

	@Override
	public void buildDescription() {
		final StringBuilder descr = new StringBuilder();
		final NpcTemplate monster = (NpcTemplate) quest.getCondition().getTarget();
		descr.append("<center>Description</center><br>");
		descr.append("It is necessary to sweep the <font color=LEVEL>")
				.append(NpcTable.getInstance().getTemplate(monster.getNpcId()).getName())
				.append("</font>, they too often prevent travelers and merchants!")
				.append(" Kill at least <font color=LEVEL>").append(quest.getCondition().getCount())
				.append("</font> individuals and come back.<br1>Do not forget to put a mark on the task (at the board) that it is done.");
		
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
		final NpcTemplate monster = ((NpcTemplate) quest.getCondition().getTarget());

		// calculate base exp for monster kill quest depends from battlerLevel of grade and monster hunt exp rewarding
		final EGradeType grade = quest.getGrade();
		
		// get random level between min and max of that grade for calculating battlerLevel
		final int battlerLevel = Rnd.get(grade.getMinLevel(), grade.getMaxLevel());
		
		// calculate exp & sp base rewards
		final double battlerValue = Math.sqrt(battlerLevel * 100.0);
		int exp = (int) (monster.getRewardExp() * battlerValue);
		int sp = (int) (monster.getRewardSp() * battlerValue);

		// if mosnter has skill HP muller we increase EXP value to them
		L2Skill hpSkill = null;
		for (List<L2Skill> list : monster.getSkills().values()) {
			for (L2Skill skill : list) {
				if (skill.getId() == 4408) {
					hpSkill = skill;
					break;
				}
			}
		}
		
		if(hpSkill != null) {
			final int level = hpSkill.getLevel();
			exp *= level;
			sp *= level;
		}
		
		// if monster has minions we give more exp and sp by 25%
		if(monster.getMinionData() != null) {
			exp *= 1.25;
			sp *= 1.25;
		}
		
		quest.setExp(exp);
		quest.setSp(sp);
	}

	@Override
	public void buildQuestItems() {
	}
}
