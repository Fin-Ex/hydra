package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("AbortCast")
public class EffectAbortCast extends L2Effect {

	public EffectAbortCast(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.ABORT_CAST;
	}

	@Override
	public boolean onStart() {
		if (getEffected() == null || getEffected() == getEffector()) {
			return false;
		}

		if (getEffected().isRaid()) {
			return false;
		}

		getEffected().breakCast();
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
