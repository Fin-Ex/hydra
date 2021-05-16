package sf.l2j.gameserver.skills.funcs;

import sf.finex.model.dye.DyeComponent;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncHennaDEX extends Func {

	static final FuncHennaDEX _fh_instance = new FuncHennaDEX();

	public static Func getInstance() {
		return _fh_instance;
	}

	private FuncHennaDEX() {
		super(Stats.DEX, 0x10, null, null);
	}

	@Override
	public void calc(Env env) {
		final DyeComponent dye = env.getPlayer().getComponent(DyeComponent.class);
		if (dye != null) {
			env.addValue(dye.getDyeDEX());
		}
	}
}
