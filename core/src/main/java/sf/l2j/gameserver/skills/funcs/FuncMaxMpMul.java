package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMaxMpMul extends Func {

	static final FuncMaxMpMul _fmmm_instance = new FuncMaxMpMul();

	public static Func getInstance() {
		return _fmmm_instance;
	}

	private FuncMaxMpMul() {
		super(Stats.MaxMP, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.MEN_BONUS[env.getCharacter().getMEN()]);
	}
}
