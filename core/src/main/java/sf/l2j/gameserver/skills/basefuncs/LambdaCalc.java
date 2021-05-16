package sf.l2j.gameserver.skills.basefuncs;

import java.util.ArrayList;
import java.util.List;
import sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public final class LambdaCalc extends Lambda {

	private final List<Func> funcs;

	public LambdaCalc() {
		funcs = new ArrayList<>();
	}

	@Override
	public double calc(Env env) {
		double saveValue = env.getValue();
		try {
			env.setValue(0);
			funcs.forEach(f -> f.calc(env));
			return env.getValue();
		} finally {
			env.setValue(saveValue);
		}
	}

	public void addFunc(Func f) {
		funcs.add(f);
	}

	public List<Func> getFuncs() {
		return funcs;
	}
}
