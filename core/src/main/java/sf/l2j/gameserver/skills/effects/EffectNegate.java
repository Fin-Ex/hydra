package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.ESkillType;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Gnat
 */
@Effect("Negate")
public class EffectNegate extends L2Effect {

	public EffectNegate(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.NEGATE;
	}

	@Override
	public boolean onStart() {
		L2Skill skill = getSkill();

		for (int negateSkillId : skill.getNegateId()) {
			if (negateSkillId != 0) {
				getEffected().stopSkillEffects(negateSkillId);
			}
		}
		for (ESkillType negateSkillType : skill.getNegateStats()) {
			getEffected().stopSkillEffects(negateSkillType, skill.getNegateLvl());
		}
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
