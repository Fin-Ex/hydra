package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Mute")
public class EffectMute extends L2Effect {

	public EffectMute(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SILENCE;
	}

	@Override
	public boolean onStart() {
		getEffected().startMuted();
		if (getEffected().isPlayer()) {
			getEffected().getPlayer().sendSkillList();
		}
		return true;
	}

	@Override
	public boolean onActionTime() {
		// Simply stop the effect
		return false;
	}

	@Override
	public void onExit() {
		getEffected().stopMuted(false);
		if (getEffected().isPlayer()) {
			getEffected().getPlayer().sendSkillList();
		}
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.MAGIC_MUTED.getMask();
	}
}
