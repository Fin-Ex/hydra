package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMAtkMod extends Func {

	static final FuncMAtkMod _fpa_instance = new FuncMAtkMod();

	public static Func getInstance() {
		return _fpa_instance;
	}

	private FuncMAtkMod() {
		super(Stats.MAtk, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		final double intb = Formulas.INT_BONUS[env.getCharacter().getINT()];
		final double lvlb = env.getCharacter().getLevelMod();

		env.mulValue((lvlb * lvlb) * (intb * intb));
	}
}
