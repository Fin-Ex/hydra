package sf.l2j.gameserver.skills.funcs;

import sf.finex.model.dye.DyeComponent;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.skills.basefuncs.Func;

public class FuncHennaINT extends Func {

	static final FuncHennaINT _fh_instance = new FuncHennaINT();

	public static Func getInstance() {
		return _fh_instance;
	}

	private FuncHennaINT() {
		super(Stats.INT, 0x10, null, null);
	}

	@Override
	public void calc(Env env) {
		final DyeComponent dye = env.getPlayer().getComponent(DyeComponent.class);
		if (dye != null) {
			env.addValue(dye.getDyeINT());
		}
	}
}
