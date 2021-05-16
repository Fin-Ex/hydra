package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Ahmed
 */
@Effect("ImmobileUntilAttacked")
public class EffectImmobileUntilAttacked extends L2Effect {

	public EffectImmobileUntilAttacked(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.IMMOBILEUNTILATTACKED;
	}

	@Override
	public boolean onStart() {
		getEffected().startImmobileUntilAttacked();
		return true;
	}

	@Override
	public void onExit() {
		getEffected().stopImmobileUntilAttacked(this);
	}

	@Override
	public boolean onActionTime() {
		getEffected().stopImmobileUntilAttacked(this);
		// just stop this effect
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.MEDITATING.getMask();
	}
}
