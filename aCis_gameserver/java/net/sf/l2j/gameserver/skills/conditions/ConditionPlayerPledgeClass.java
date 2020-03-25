package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerPledgeClass.
 *
 * @author MrPoke
 */
public final class ConditionPlayerPledgeClass extends Condition {

	private int pledgeClass;

	/**
	 * Instantiates a new condition player pledge class.
	 *
	 * @param pledgeClass the pledge class
	 */
	public ConditionPlayerPledgeClass(int pledgeClass) {
		this.pledgeClass = pledgeClass;
	}

	/**
	 * Test impl.
	 *
	 * @param env the env
	 * @return true, if successful
	 */
	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() == null) {
			return false;
		}

		if (env.getPlayer().getClan() == null) {
			return false;
		}

		if (pledgeClass == -1) {
			return env.getPlayer().isClanLeader();
		}

		return env.getPlayer().getPledgeClass() >= pledgeClass;
	}
}
