package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public class ConditionPlayerLevel extends Condition {

	private int level;

	public ConditionPlayerLevel(int level) {
		this.level = level;
	}

	@Override
	public boolean testImpl(Env env) {
		return env.getCharacter().getLevel() >= level;
	}
}
