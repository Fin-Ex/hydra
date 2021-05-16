package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import sf.l2j.gameserver.model.actor.instance.Monster;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * This is the Effect support for spoil, originally done by _drunk_
 *
 * @author Ahmed
 */
@Effect("Spoil")
public class EffectSpoil extends L2Effect {

	public EffectSpoil(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SPOIL;
	}

	@Override
	public boolean onStart() {
		if (!(getEffector() instanceof Player)) {
			return false;
		}

		if (!(getEffected() instanceof Monster)) {
			return false;
		}

		final Monster target = (Monster) getEffected();
		if (target.isDead()) {
			return false;
		}

		if (target.getSpoilerId() != 0) {
			getEffector().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_SPOILED));
			return false;
		}

		if (Formulas.calcMagicSuccess(getEffector(), target, getSkill())) {
			target.setSpoilerId(getEffector().getObjectId());
			getEffector().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SPOIL_SUCCESS));
		}
		target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, getEffector());
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
