package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

public class ConditionPlayerHpPercentage extends Condition {

	private double percent;

	public ConditionPlayerHpPercentage(double percent) {
		this.percent = percent;
	}

	@Override
	public boolean testImpl(Env env) {
		return env.getCharacter().getCurrentHp() <= env.getCharacter().getMaxHp() * percent;
	}
}
