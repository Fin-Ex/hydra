package sf.l2j.gameserver.skills.l2skills;

import sf.finex.enums.ESkillTargetType;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.skills.effects.EffectSeed;
import sf.l2j.gameserver.templates.StatsSet;
import sf.l2j.gameserver.templates.skills.L2EffectType;

public class L2SkillSeed extends L2Skill {

	public L2SkillSeed(StatsSet set) {
		super(set);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		if (caster.isAlikeDead()) {
			return;
		}

		// Update Seeds Effects
		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isAlikeDead() && getTargetType() != ESkillTargetType.TARGET_CORPSE_MOB) {
				continue;
			}

			EffectSeed oldEffect = (EffectSeed) target.getFirstEffect(getId());
			if (oldEffect == null) {
				getEffects(caster, target);
			} else {
				oldEffect.increasePower();
			}

			L2Effect[] effects = target.getAllEffects();
			for (L2Effect effect : effects) {
				if (effect.getEffectType() == L2EffectType.SEED) {
					effect.rescheduleEffect();
				}
			}
		}
	}
}
