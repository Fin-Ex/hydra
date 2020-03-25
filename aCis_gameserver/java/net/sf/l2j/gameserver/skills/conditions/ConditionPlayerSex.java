package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerSex.
 */
public class ConditionPlayerSex extends Condition {

	private int sex;

	/**
	 * Instantiates a new condition player sex.
	 *
	 * @param sex the sex
	 */
	public ConditionPlayerSex(int sex) {
		this.sex = sex;
	}

	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() == null) {
			return false;
		}

		return env.getPlayer().getAppearance().getSex().ordinal() == sex;
	}
}
