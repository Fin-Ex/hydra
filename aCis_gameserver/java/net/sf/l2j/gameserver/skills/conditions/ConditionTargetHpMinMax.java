package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * Used for Trap skills.
 *
 * @author Tryskell
 */
public class ConditionTargetHpMinMax extends Condition {

	private int minHP, maxHP;

	public ConditionTargetHpMinMax(int minHp, int maxHp) {
		this.minHP = minHp;
		this.maxHP = maxHp;
	}

	@Override
	public boolean testImpl(Env env) {
		if (env.getTarget() == null) {
			return false;
		}

		int curHP = (int) env.getTarget().getCurrentHp() * 100 / env.getTarget().getMaxHp();
		return curHP >= minHP && curHP <= maxHP;
	}
}
