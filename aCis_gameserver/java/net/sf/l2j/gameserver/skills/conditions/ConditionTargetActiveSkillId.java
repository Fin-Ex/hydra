package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionTargetActiveSkillId.
 */
public class ConditionTargetActiveSkillId extends Condition {

	private int skillId;

	/**
	 * Instantiates a new condition target active skill id.
	 *
	 * @param skillId the skill id
	 */
	public ConditionTargetActiveSkillId(int skillId) {
		this.skillId = skillId;
	}

	@Override
	public boolean testImpl(Env env) {
		return env.getTarget().getSkill(skillId) != null;
	}
}
