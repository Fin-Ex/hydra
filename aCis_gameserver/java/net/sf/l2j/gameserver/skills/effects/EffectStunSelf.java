package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("StunSelf")
public class EffectStunSelf extends L2Effect {

	public EffectStunSelf(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.STUN_SELF;
	}

	@Override
	public boolean onStart() {
		getEffector().startStunning();
		return true;
	}

	@Override
	public void onExit() {
		getEffector().stopStunning(false);
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public boolean isSelfEffectType() {
		return true;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.STUNNED.getMask();
	}
}
