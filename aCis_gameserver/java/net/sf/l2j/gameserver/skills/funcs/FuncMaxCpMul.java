package net.sf.l2j.gameserver.skills.funcs;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMaxCpMul extends Func {

	static final FuncMaxCpMul _fmcm_instance = new FuncMaxCpMul();

	public static Func getInstance() {
		return _fmcm_instance;
	}

	private FuncMaxCpMul() {
		super(Stats.MaxCP, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		env.mulValue(Formulas.CON_BONUS[env.getCharacter().getCON()]);
	}
}
