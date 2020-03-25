package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionLogicAnd.
 *
 * @author mkizub
 */
public class ConditionLogicAnd extends ConditionLogic {

	@Override
	public boolean testImpl(Env env) {
		for (Condition c : getConditions()) {
			if (!c.test(env)) {
				return false;
			}
		}
		return true;
	}
}
