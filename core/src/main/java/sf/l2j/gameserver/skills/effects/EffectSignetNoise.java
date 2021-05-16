package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.instance.EffectPoint;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @authors Forsaiken, Sami
 */
@Effect("SignetNoise")
public class EffectSignetNoise extends L2Effect {

	private EffectPoint _actor;

	public EffectSignetNoise(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SIGNET_GROUND;
	}

	@Override
	public boolean onStart() {
		_actor = (EffectPoint) getEffected();
		return true;
	}

	@Override
	public boolean onActionTime() {
		if (getCount() == getTotalCount() - 1) {
			return true; // do nothing first time
		}
		Player caster = (Player) getEffector();

		for (Creature target : _actor.getKnownTypeInRadius(Creature.class, getSkill().getSkillRadius())) {
			if (target == caster) {
				continue;
			}

			if (caster.canAttackCharacter(target)) {
				for (L2Effect effect : target.getAllEffects()) {
					if (effect.getSkill().isDance()) {
						effect.exit();
					}
				}
			}
		}
		return true;
	}

	@Override
	public void onExit() {
		if (_actor != null) {
			_actor.deleteMe();
		}
	}
}
