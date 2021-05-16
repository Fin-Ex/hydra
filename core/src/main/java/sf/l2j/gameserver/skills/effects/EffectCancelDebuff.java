package sf.l2j.gameserver.skills.effects;

import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.ESkillType;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author UnAfraid
 */
@Effect("CancelDebuff")
public class EffectCancelDebuff extends L2Effect {

	public EffectCancelDebuff(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.CANCEL_DEBUFF;
	}

	@Override
	public boolean onStart() {
		return cancel(getEffector(), getEffected(), getSkill(), getEffectTemplate().effectType);
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	private static boolean cancel(Creature caster, Creature target, L2Skill skill, ESkillType effectType) {
		if (!(target instanceof Player) || target.isDead()) {
			return false;
		}

		final int cancelLvl = skill.getMagicLevel();
		int count = skill.getMaxNegatedEffects();
		double baseRate = Formulas.calcSkillVulnerability(caster, target, skill, effectType);

		L2Effect effect;
		int lastCanceledSkillId = 0;
		final L2Effect[] effects = target.getAllEffects();
		for (int i = effects.length; --i >= 0;) {
			effect = effects[i];
			if (effect == null) {
				continue;
			}

			if (!effect.getSkill().isDebuff() || !effect.getSkill().canBeDispeled()) {
				effects[i] = null;
				continue;
			}

			if (effect.getSkill().getId() == lastCanceledSkillId) {
				effect.exit(); // this skill already canceled
				continue;
			}

			if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate)) {
				continue;
			}

			lastCanceledSkillId = effect.getSkill().getId();
			effect.exit();
			count--;

			if (count == 0) {
				break;
			}
		}

		if (count != 0) {
			lastCanceledSkillId = 0;
			for (int i = effects.length; --i >= 0;) {
				effect = effects[i];
				if (effect == null) {
					continue;
				}

				if (!effect.getSkill().isDebuff() || !effect.getSkill().canBeDispeled()) {
					effects[i] = null;
					continue;
				}

				if (effect.getSkill().getId() == lastCanceledSkillId) {
					effect.exit(); // this skill already canceled
					continue;
				}

				if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate)) {
					continue;
				}

				lastCanceledSkillId = effect.getSkill().getId();
				effect.exit();
				count--;

				if (count == 0) {
					break;
				}
			}
		}
		return true;
	}

	private static boolean calcCancelSuccess(L2Effect effect, int cancelLvl, int baseRate) {
		int rate = 2 * (cancelLvl - effect.getSkill().getMagicLevel());
		rate += (effect.getPeriod() - effect.getTime()) / 1200;
		rate *= baseRate;

		if (rate < 25) {
			rate = 25;
		} else if (rate > 75) {
			rate = 75;
		}

		return Rnd.get(100) < rate;
	}
}
