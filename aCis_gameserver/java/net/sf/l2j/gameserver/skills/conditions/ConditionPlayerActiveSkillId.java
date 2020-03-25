package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * The Class ConditionPlayerActiveSkillId.
 *
 * @author DrHouse
 */
public class ConditionPlayerActiveSkillId extends Condition {

	private int skillId;
	private int skillLevel;

	/**
	 * Instantiates a new condition player active skill id.
	 *
	 * @param skillId the skill id
	 */
	public ConditionPlayerActiveSkillId(int skillId) {
		this.skillId = skillId;
		this.skillLevel = -1;
	}

	/**
	 * Instantiates a new condition player active skill id.
	 *
	 * @param skillId the skill id
	 * @param skillLevel the skill level
	 */
	public ConditionPlayerActiveSkillId(int skillId, int skillLevel) {
		this.skillId = skillId;
		this.skillLevel = skillLevel;
	}

	@Override
	public boolean testImpl(Env env) {
		final L2Skill skill = env.getCharacter().getSkill(skillId);
		return skill != null && skillLevel <= skill.getLevel();
	}
}
