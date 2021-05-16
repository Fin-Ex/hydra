package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author -Nemesiss-
 */
@Effect("TargetMe")
public class EffectTargetMe extends L2Effect {

	public EffectTargetMe(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.TARGET_ME;
	}

	@Override
	public boolean onStart() {
		// work only on players, cause mobs got their own aggro system (AGGDAMAGE, AGGREMOVE, etc)
		if (getEffected() instanceof Player) {
			// add an INTENTION_ATTACK, but only if victim got attacker as target
			if ((getEffected().getAI() == null || getEffected().getAI().getNextIntention() == null) && getEffected().getTarget() == getEffector()) {
				getEffected().getAI().setIntention(CtrlIntention.ATTACK, getEffector());
			}

			// target the agressor
			getEffected().setTarget(getEffector());
			return true;
		}
		return false;
	}

	@Override
	public void onExit() {
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
