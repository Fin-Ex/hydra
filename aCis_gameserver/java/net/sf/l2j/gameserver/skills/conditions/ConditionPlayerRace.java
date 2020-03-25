package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.base.ClassRace;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public class ConditionPlayerRace extends Condition {

	private ClassRace race;

	public ConditionPlayerRace(ClassRace race) {
		this.race = race;
	}

	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() == null) {
			return false;
		}

		return env.getPlayer().getRace() == race;
	}
}
