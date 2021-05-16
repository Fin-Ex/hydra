package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.model.actor.Summon;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncAtkCritical extends Func {

	static final FuncAtkCritical _fac_instance = new FuncAtkCritical();

	public static Func getInstance() {
		return _fac_instance;
	}

	private FuncAtkCritical() {
		super(Stats.CriticalRate, 0x09, null, null);
	}

	@Override
	public void calc(Env env) {
		if (!(env.getCharacter() instanceof Summon)) {
			env.mulValue(Formulas.DEX_BONUS[env.getCharacter().getDEX()]);
		}

		env.mulValue(10);

		env.setBaseValue(env.getValue());
	}
}
