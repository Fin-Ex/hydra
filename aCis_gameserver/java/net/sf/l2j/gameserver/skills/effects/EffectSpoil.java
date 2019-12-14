package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

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
