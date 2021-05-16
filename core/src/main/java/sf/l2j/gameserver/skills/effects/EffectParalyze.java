package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.AbnormalEffect;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Paralyze")
public class EffectParalyze extends L2Effect {

	public EffectParalyze(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.PARALYZE;
	}

	@Override
	public boolean onStart() {
		getEffected().startAbnormalEffect(AbnormalEffect.HOLD_1);
		getEffected().startParalyze();
		return true;
	}

	@Override
	public void onExit() {
		getEffected().stopAbnormalEffect(AbnormalEffect.HOLD_1);
		getEffected().stopParalyze(false);
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.PARALYZED.getMask();
	}
}
