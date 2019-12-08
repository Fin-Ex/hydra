package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("SilenceMagicPhysical")
public class EffectSilenceMagicPhysical extends L2Effect {

	public EffectSilenceMagicPhysical(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SILENCE;
	}

	@Override
	public boolean onStart() {
		getEffected().startMuted();
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public void onExit() {
		getEffected().stopMuted(false);
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.MAGIC_MUTED.getMask() | EEffectFlag.PHYSICAL_MUTED.getMask();
	}
}
