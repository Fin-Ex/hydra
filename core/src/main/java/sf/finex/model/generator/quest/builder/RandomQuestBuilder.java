/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.generator.quest.builder;

import sf.finex.data.RandomQuestData;

/**
 *
 * @author FinFan
 */
public abstract class RandomQuestBuilder {

	protected RandomQuestData quest;

	public final RandomQuestData create() {
		return this.quest = new RandomQuestData();
	}

	public abstract void buildCondition();

	public abstract void buildRewards();

	public abstract void buildDescription();

	public abstract void buildName();

	public abstract void buildExpAndSp();

	public abstract void buildQuestItems();
}
