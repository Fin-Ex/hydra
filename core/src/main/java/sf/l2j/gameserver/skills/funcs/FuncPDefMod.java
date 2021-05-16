package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncPDefMod extends Func {

	static final FuncPDefMod _fpa_instance = new FuncPDefMod();

	public static Func getInstance() {
		return _fpa_instance;
	}

	private FuncPDefMod() {
		super(Stats.PDef, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		final Creature cha = env.getCharacter();
		final double CONbonus = Formulas.CON_BONUS[cha.getCON()];
		final double modifier = (CONbonus / 10. + 1) * cha.getLevelMod();
		env.mulValue(modifier);
	}
}
