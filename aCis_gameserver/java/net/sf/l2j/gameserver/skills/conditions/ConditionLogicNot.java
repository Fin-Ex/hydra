package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionLogicNot.
 *
 * @author mkizub
 */
public class ConditionLogicNot extends ConditionLogic {

	@Override
	public boolean testImpl(Env env) {
		for (Condition condition : getConditions()) {
			if (condition.test(env)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isLogic() {
		return true;
	}
}
