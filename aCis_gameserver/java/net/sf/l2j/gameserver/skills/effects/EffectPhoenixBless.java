package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

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
