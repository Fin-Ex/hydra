package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("BlockBuff")
public class EffectBlockBuff extends L2Effect {

	public EffectBlockBuff(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BLOCK_BUFF;
	}

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
