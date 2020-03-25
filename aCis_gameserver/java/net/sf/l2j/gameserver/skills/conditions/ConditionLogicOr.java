package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionLogicOr.
 *
 * @author mkizub
 */
public class ConditionLogicOr extends ConditionLogic {

	@Override
	public boolean testImpl(Env env) {
		for (Condition c : getConditions()) {
			if (c.test(env)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isLogic() {
		return true;
	}
}
