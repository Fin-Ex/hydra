package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.Summon;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author demonia
 */
@Effect("ImobilePetBuff")
final class EffectImobilePetBuff extends L2Effect {

	private Summon _pet;

	public EffectImobilePetBuff(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BUFF;
	}

	@Override
	public boolean onStart() {
		_pet = null;

		if (getEffected() instanceof Summon && getEffector() instanceof Player && ((Summon) getEffected()).getPlayer() == getEffector()) {
			_pet = (Summon) getEffected();
			_pet.setIsImmobilized(true);
			return true;
		}
		return false;
	}

	@Override
	public void onExit() {
		_pet.setIsImmobilized(false);
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
