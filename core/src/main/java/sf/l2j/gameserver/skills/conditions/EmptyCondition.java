package sf.l2j.gameserver.skills.conditions;

import sf.l2j.gameserver.skills.Env;

/**
 * @author mangol
 */
public class EmptyCondition extends ConditionLogic {
	private static transient final EmptyCondition instance = new EmptyCondition();

	public static EmptyCondition getInstance() {
		return instance;
	}

	@Override
	boolean testImpl(Env env) {
		return true;
	}
}
