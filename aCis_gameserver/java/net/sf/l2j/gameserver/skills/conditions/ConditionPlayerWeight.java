package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerWeight.
 *
 * @author Kerberos
 */
public class ConditionPlayerWeight extends Condition {

	private int weight;

	/**
	 * Instantiates a new condition player weight.
	 *
	 * @param weight the weight
	 */
	public ConditionPlayerWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public boolean testImpl(Env env) {
		final Player player = env.getPlayer();
		if (player != null && player.getMaxLoad() > 0) {
			int weightproc = player.getCurrentLoad() * 100 / player.getMaxLoad();
			return weightproc < weight;
		}
		return true;
	}
}
