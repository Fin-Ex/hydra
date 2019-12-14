/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.builder;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.RandomQuestConditionData;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.model.generator.quest.RandomQuestManager;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * Monster killing quest.<br>
 * Description: Kill some monster count and return to quest board. Get reward
 * model <b>[Adena]</b> which depends from your level and quest difficult. Exp &
 * SP player gets on hunt phase.
 *
 * @author FinFan
 */
@Slf4j
public class MonsterKillBuild extends RandomQuestBuilder {

	@Override
	public void buildCondition() {
		final Map<Integer, List<NpcTemplate>> monstersMap = RandomQuestManager.getInstance().getMonsters();
		final NpcTemplate monsterForHunt = Rnd.get(monstersMap.entrySet().stream()
				.filter(e -> e.getKey() >= quest.getGrade().getMinLevel() && e.getKey() <= quest.getGrade().getMaxLevel())
				.flatMap(e -> e.getValue().stream())
				.toArray(NpcTemplate[]::new)
		);

		int monsterKillCount = 0;
		switch (quest.getGrade()) {
			case NG:
				monsterKillCount = 10;
				break;

			case D:
				monsterKillCount = 20;
				break;

			case C:
				monsterKillCount = 40;
				break;

			case B:
				monsterKillCount = 80;
				break;

			case A:
				monsterKillCount = 160;
				break;

			case S:
				monsterKillCount = 320;
				break;
		}

		quest.setCondition(new RandomQuestConditionData(quest.getType(), monsterForHunt, monsterKillCount));
	}

	@Override
	public void buildRewards() {
	}

	@Override
	public void buildDescription() {
		final StringBuilder descr = new StringBuilder();
		final NpcTemplate monster = (NpcTemplate) quest.getCondition().getTarget();
		descr.append("<center>Description</center><br>");
		descr.append("It is necessary to sweep the <font color=LEVEL>")
				.append(NpcTable.getInstance().getTemplate(monster.getNpcId()).getName())
				.append("</font>, they too often prevent travelers and merchants!")
				.append(" Kill at least <font color=LEVEL>").append(quest.getCondition().getValue())
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

		// calculate exp & sp base rewards
		long exp = monster.getRewardExp();
		int sp = monster.getRewardSp();

		// if mosnter has skill HP muller we increase EXP value to them
		for (List<L2Skill> list : monster.getSkills().values()) {
			for (L2Skill skill : list) {
				if (skill.getId() == 4408) {
					final int level = skill.getLevel();
					exp *= level;
					sp *= level;
					break;
				}
			}
		}

		// if monster has minions we give more exp and sp by 25%
		if (!monster.getMinionData().isEmpty()) {
			final float minionCount = monster.getMinionData().size();
			exp *= minionCount;
			sp *= minionCount;
		}

		final int value = quest.getCondition().getValue();
		exp *= value;
		sp *= value;

		quest.setExp(exp);
		quest.setSp(sp);
	}

	@Override
	public void buildQuestItems() {
	}
}
