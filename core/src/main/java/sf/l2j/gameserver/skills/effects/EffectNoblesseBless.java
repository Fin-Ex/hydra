package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author earendil
 */
@Effect("NoblesseBless")
public class EffectNoblesseBless extends L2Effect {

	public EffectNoblesseBless(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.NOBLESSE_BLESSING;
	}

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public void onExit() {
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.NOBLESS_BLESSING.getMask();
	}
}
