package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author mkizub
 */
@Effect("Buff")
public class EffectBuff extends L2Effect {

	public EffectBuff(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BUFF;
	}

	@Override
	public boolean onActionTime() {
		// just stop this effect
		return false;
	}
}
