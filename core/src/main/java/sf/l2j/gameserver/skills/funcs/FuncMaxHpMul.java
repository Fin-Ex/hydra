package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMaxHpMul extends Func {

	static final FuncMaxHpMul _fmhm_instance = new FuncMaxHpMul();

	public static Func getInstance() {
		return _fmhm_instance;
	}

	private FuncMaxHpMul() {
		super(Stats.MaxHP, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.CON_BONUS[env.getCharacter().getCON()]);
	}
}
