package sf.l2j.gameserver.skills.basefuncs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Stats;

public class FuncBaseMul extends Func {

	public FuncBaseMul(Stats pStat, int pOrder, Object owner, Lambda lambda) {
		super(pStat, pOrder, owner, lambda);
	}

	@Override
	public void calc(Env env) {
		if (cond == null || cond.test(env)) {
			double finalValue = lambda.calc(env);
			if (effectBonus > 0) {
				finalValue *= effectBonus;
			}
			if (skillBonus > 0) {
				finalValue *= skillBonus;
			}
			env.addValue(env.getBaseValue() * finalValue);
		}
	}
}
