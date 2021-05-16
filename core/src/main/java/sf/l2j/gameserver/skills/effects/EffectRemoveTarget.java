package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author -Nemesiss-
 */
@Effect("RemoveTarget")
public class EffectRemoveTarget extends L2Effect {

	public EffectRemoveTarget(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.REMOVE_TARGET;
	}

	@Override
	public boolean onStart() {
		getEffected().removeTarget();
		getEffected().getAI().setIntention(CtrlIntention.IDLE, getEffector());
		return true;
	}

	@Override
	public void onExit() {
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
