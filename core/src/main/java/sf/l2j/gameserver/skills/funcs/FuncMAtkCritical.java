package sf.l2j.gameserver.skills.funcs;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncMAtkCritical extends Func {

	static final FuncMAtkCritical _fac_instance = new FuncMAtkCritical();

	public static Func getInstance() {
		return _fac_instance;
	}

	private FuncMAtkCritical() {
		super(Stats.MagicCriticalRate, 0x30, null, null);
	}

	@Override
	public void calc(Env env) {
		final Creature player = env.getCharacter();
		if (player instanceof Player) {
			if (player.getActiveWeaponInstance() != null) {
				env.mulValue(Formulas.WIT_BONUS[player.getWIT()]);
			}
		} else {
			env.mulValue(Formulas.WIT_BONUS[player.getWIT()]);
		}
	}
}
