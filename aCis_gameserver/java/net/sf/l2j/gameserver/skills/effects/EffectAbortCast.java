package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

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
