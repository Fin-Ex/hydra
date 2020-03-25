package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerInvSize.
 *
 * @author Kerberos
 */
public class ConditionPlayerInvSize extends Condition {

	private int size;

	/**
	 * Instantiates a new condition player inv size.
	 *
	 * @param size the size
	 */
	public ConditionPlayerInvSize(int size) {
		this.size = size;
	}

	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() != null) {
			return env.getPlayer().getInventory().getSize() <= (env.getPlayer().getInventoryLimit() - size);
		}

		return true;
	}
}
