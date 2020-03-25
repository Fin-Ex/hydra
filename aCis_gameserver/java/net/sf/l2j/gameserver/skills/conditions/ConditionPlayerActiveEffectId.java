package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;

/**
 * The Class ConditionPlayerActiveEffectId.
 */
public class ConditionPlayerActiveEffectId extends Condition {

	private int effectId;
	private int effectLevel;

	/**
	 * Instantiates a new condition player active effect id.
	 *
	 * @param effectId the effect id
	 */
	public ConditionPlayerActiveEffectId(int effectId) {
		this.effectId = effectId;
		this.effectLevel = -1;
	}

	/**
	 * Instantiates a new condition player active effect id.
	 *
	 * @param effectId the effect id
	 * @param effectLevel the effect level
	 */
	public ConditionPlayerActiveEffectId(int effectId, int effectLevel) {
		this.effectId = effectId;
		this.effectLevel = effectLevel;
	}

	@Override
	public boolean testImpl(Env env) {
		final L2Effect e = env.getCharacter().getFirstEffect(effectId);
		return e != null && (effectLevel == -1 || effectLevel <= e.getSkill().getLevel());
	}
}
