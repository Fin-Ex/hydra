package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author kerberos_20
 */
@Effect("CharmOfLuck")
public class EffectCharmOfLuck extends L2Effect {

	public EffectCharmOfLuck(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.CHARM_OF_LUCK;
	}

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public void onExit() {
		((Playable) getEffected()).stopCharmOfLuck(this);
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.CHARM_OF_LUCK.getMask();
	}
}
