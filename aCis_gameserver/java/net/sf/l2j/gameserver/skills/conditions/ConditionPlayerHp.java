package net.sf.l2j.gameserver.skills.conditions;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mr
 */
public class ConditionPlayerHp extends Condition {

	private final int _hp;

	public ConditionPlayerHp(int hp) {
		_hp = hp;
	}

	@Override
	public boolean testImpl(Env env) {
		final double currentHP = env.getCharacter().getCurrentHp() * 100 / env.getCharacter().getMaxHp();
		if (env.getSkill() != null) {
			switch (env.getSkill().getId()) {
				case 181:
					if (env.getPlayer().hasTalent(18)) {
						return currentHP <= 15.0;
					}
			}
		}
		return currentHP <= _hp;
	}
}
