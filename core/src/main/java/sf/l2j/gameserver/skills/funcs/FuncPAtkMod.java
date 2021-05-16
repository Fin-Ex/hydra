package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncPAtkMod extends Func {

	static final FuncPAtkMod _fpa_instance = new FuncPAtkMod();

	public static Func getInstance() {
		return _fpa_instance;
	}

	private FuncPAtkMod() {
		super(Stats.PAtk, 0x30, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.STR_BONUS[env.getCharacter().getSTR()] * env.getCharacter().getLevelMod());
	}
}
