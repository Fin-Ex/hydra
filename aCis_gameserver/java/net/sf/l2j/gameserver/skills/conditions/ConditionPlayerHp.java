package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mr
 */
public class ConditionPlayerHp extends Condition {

	private int hp;

	public ConditionPlayerHp(int hp) {
		this.hp = hp;
	}

	@Override
	public boolean testImpl(Env env) {
		final double currentHP = env.getCharacter().getCurrentHp() * 100 / env.getCharacter().getMaxHp();
		return currentHP <= hp;
	}
}
