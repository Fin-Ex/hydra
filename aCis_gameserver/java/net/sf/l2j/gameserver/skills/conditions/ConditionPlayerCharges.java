package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerCharges.
 */
public class ConditionPlayerCharges extends Condition {

	private int charges;

	/**
	 * Instantiates a new condition player charges.
	 *
	 * @param charges the charges
	 */
	public ConditionPlayerCharges(int charges) {
		this.charges = charges;
	}

	@Override
	public boolean testImpl(Env env) {
		return env.getPlayer() != null && env.getPlayer().getCharges() >= charges;
	}
}
