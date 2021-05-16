package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Invincible")
public class EffectInvincible extends L2Effect {

	public EffectInvincible(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.INVINCIBLE;
	}

	@Override
	public boolean onStart() {
		getEffected().setIsInvul(true);
		return super.onStart();
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public void onExit() {
		getEffected().setIsInvul(false);
		super.onExit();
	}
}
