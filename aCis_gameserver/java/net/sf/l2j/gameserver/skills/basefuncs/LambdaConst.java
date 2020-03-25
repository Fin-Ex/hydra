package net.sf.l2j.gameserver.skills.basefuncs;

import lombok.Getter;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public final class LambdaConst extends Lambda {

	@Getter private final double value;

	public LambdaConst(double value) {
		this.value = value;
	}

	@Override
	public double calc(Env env) {
		return value;
	}
}
