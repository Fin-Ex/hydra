package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Faror
 */
@Effect("PhoenixBless")
public class EffectPhoenixBless extends L2Effect {

	public EffectPhoenixBless(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.PHOENIX_BLESSING;
	}

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public void onExit() {
		if (getEffected() instanceof Playable) {
			((Playable) getEffected()).stopPhoenixBlessing(this);
		}
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.PHOENIX_BLESSING.getMask();
	}
}
