package sf.l2j.gameserver.skills.conditions;


import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Stats;

/**
 * @author mkizub
 */
public class ConditionSkillStats extends Condition {

	private Stats stat;

	public ConditionSkillStats(Stats stat) {
		super();
		this.stat = stat;
	}

	@Override
	public boolean testImpl(Env env) {
		return env.getSkill() != null && env.getSkill().getStat() == stat;
	}
}
