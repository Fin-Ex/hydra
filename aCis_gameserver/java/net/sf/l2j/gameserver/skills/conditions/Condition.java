package net.sf.l2j.gameserver.skills.conditions;

import lombok.Getter;
import lombok.Setter;
import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class Condition.
 *
 * @author mkizub
 */
public abstract class Condition {

	protected final transient static Condition[] EMPTY_CONDITIONS = new Condition[0];
	
	@Getter @Setter private String message;
	@Getter @Setter private int messageId;
	@Getter @Setter private boolean addName;
	@Getter @Setter private boolean result;

	public final boolean test(Env env) {
		return testImpl(env);
	}

	abstract boolean testImpl(Env env);
	
	public boolean isLogic() {
		return false;
	}
}
