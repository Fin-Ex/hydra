package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Kerberos
 */
@Effect("Recovery")
public class EffectRecovery extends L2Effect {

	public EffectRecovery(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BUFF;
	}

	@Override
	public boolean onStart() {
		if (getEffected() instanceof Player) {
			((Player) getEffected()).reduceDeathPenaltyBuffLevel();
			return true;
		}
		return false;
	}

	@Override
	public void onExit() {
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
