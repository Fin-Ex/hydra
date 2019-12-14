package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Seed")
public final class EffectSeed extends L2Effect {

	private int _power = 1;

	public EffectSeed(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SEED;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	public int getPower() {
		return _power;
	}

	public void increasePower() {
		_power++;
	}
}
