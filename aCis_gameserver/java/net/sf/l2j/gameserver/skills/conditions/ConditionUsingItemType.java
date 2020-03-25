package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public final class ConditionUsingItemType extends Condition {

	private int mask;

	public ConditionUsingItemType(int mask) {
		this.mask = mask;
	}

	@Override
	public boolean testImpl(Env env) {
		if (!(env.getCharacter() instanceof Player)) {
			return false;
		}

		return (mask & env.getPlayer().getInventory().getWornMask()) != 0;
	}
}
