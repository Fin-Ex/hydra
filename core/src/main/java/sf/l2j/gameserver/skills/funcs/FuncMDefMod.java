package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMDefMod extends Func {

	static final FuncMDefMod _fpa_instance = new FuncMDefMod();

	public static Func getInstance() {
		return _fpa_instance;
	}

	private FuncMDefMod() {
		super(Stats.MDef, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.MEN_BONUS[env.getCharacter().getMEN()] * env.getCharacter().getLevelMod());
	}
}
