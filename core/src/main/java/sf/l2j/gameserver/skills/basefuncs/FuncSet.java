package sf.l2j.gameserver.skills.basefuncs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Stats;

public class FuncSet extends Func {

	public FuncSet(Stats pStat, int pOrder, Object owner, Lambda lambda) {
		super(pStat, pOrder, owner, lambda);
	}

	@Override
	public void calc(Env env) {
		if (cond == null || cond.test(env)) {
			env.setValue(lambda.calc(env));
		}
	}
}
