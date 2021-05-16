package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMoveSpeed extends Func {

	static final FuncMoveSpeed _fms_instance = new FuncMoveSpeed();

	public static Func getInstance() {
		return _fms_instance;
	}

	private FuncMoveSpeed() {
		super(Stats.Speed, 0x30, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.DEX_BONUS[env.getCharacter().getDEX()]);
	}
}
