package sf.l2j.gameserver.skills.conditions;


import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.skills.Env;

/**
 * @author Advi
 */
public class ConditionGameChance extends Condition {

	private int chance;

	public ConditionGameChance(int chance) {
		this.chance = chance;
	}

	@Override
	public boolean testImpl(Env env) {
		return Rnd.get(100) < chance;
	}
}
