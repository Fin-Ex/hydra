package sf.l2j.gameserver.skills.funcs;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

/**
 * Increase speed of realoding arrows (Bow attack).
 *
 * @author FinFan
 */
@Slf4j
public class FuncArrowReloadSpd extends Func {

	static final FuncArrowReloadSpd _faa_instance = new FuncArrowReloadSpd();

	public static Func getInstance() {
		return _faa_instance;
	}

	private FuncArrowReloadSpd() {
		super(Stats.ArrowReloadSpd, 0x20, null, null);
	}

	@Override
	public void calc(Env env) {
		final double arrowSpdMod = Formulas.DEX_BONUS[env.getCharacter().getDEX()];
		log.info("Arrow reload mod: {}", arrowSpdMod);
		env.divValue(arrowSpdMod);
	}
}
