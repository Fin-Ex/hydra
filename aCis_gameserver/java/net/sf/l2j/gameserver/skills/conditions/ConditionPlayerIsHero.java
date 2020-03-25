package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerIsHero.
 */
public class ConditionPlayerIsHero extends Condition {

	private boolean value;

	/**
	 * Instantiates a new condition player is hero.
	 *
	 * @param value the val
	 */
	public ConditionPlayerIsHero(boolean value) {
		this.value = value;
	}

	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() == null) {
			return false;
		}

		return (env.getPlayer().isHero() == value);
	}
}
