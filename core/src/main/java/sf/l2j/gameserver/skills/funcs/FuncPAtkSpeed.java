package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncPAtkSpeed extends Func {

	static final FuncPAtkSpeed _fas_instance = new FuncPAtkSpeed();

	public static Func getInstance() {
		return _fas_instance;
	}

	private FuncPAtkSpeed() {
		super(Stats.PAtkSpd, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.DEX_BONUS[env.getCharacter().getDEX()]);
	}
}
