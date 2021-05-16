/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.enums;

import lombok.Getter;
import sf.finex.data.RandomQuestData;
import sf.finex.model.generator.quest.RandomQuestIdFactory;
import sf.finex.model.generator.quest.builder.BossHuntBuilder;
import sf.finex.model.generator.quest.builder.DeliverItemBuilder;
import sf.finex.model.generator.quest.builder.FindResourcesBuilder;
import sf.finex.model.generator.quest.builder.MonsterKillBuild;
import sf.finex.model.generator.quest.builder.RandomQuestBuilder;
import sf.finex.model.generator.quest.handler.BossHuntHandler;
import sf.finex.model.generator.quest.handler.DeliverItemHandler;
import sf.finex.model.generator.quest.handler.FindResourceHandler;
import sf.finex.model.generator.quest.handler.MonsterKillHandler;
import sf.finex.model.generator.quest.handler.RandomQuestHandler;

/**
 *
 * @author FinFan
 */
public enum ERandomQuestType {
	Monster_Kill("Killing the ", new MonsterKillBuild(), new MonsterKillHandler()),
	Find_Resource("In search of ", new FindResourcesBuilder(), new FindResourceHandler()),
	Item_Deliver("We need ", new DeliverItemBuilder(), new DeliverItemHandler()),
	Boss_Hunt("Great Hunt: ", new BossHuntBuilder(), new BossHuntHandler());

	@Getter
	private final String questName;
	@Getter
	private final RandomQuestBuilder builder;
	@Getter
	private final RandomQuestHandler handler;

	private ERandomQuestType(String questName, RandomQuestBuilder builder, RandomQuestHandler handler) {
		this.questName = questName;
		this.builder = builder;
		this.handler = handler;
	}

	public RandomQuestData create(ERandomQuestType type, ETownType town, EGradeType grade) {
		final RandomQuestData quest = builder.create();
		quest.setId(RandomQuestIdFactory.getInstance().nextId());
		quest.setType(type);
		quest.setTown(town);
		quest.setGrade(grade);
		builder.buildQuestItems();
		builder.buildCondition();
		builder.buildDescription();
		builder.buildName();
		builder.buildRewards();
		builder.buildExpAndSp();
		return quest;
	}

	@Override
	public String toString() {
		return name().replace("_", " ");
	}
}
