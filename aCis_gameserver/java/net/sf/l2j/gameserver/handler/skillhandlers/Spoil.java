package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author _drunk_
 */
public class Spoil implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.SPOIL
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		if (targets == null) {
			return;
		}

		for (WorldObject tgt : targets) {
			if (!(tgt instanceof Monster)) {
				continue;
			}

			final Monster target = (Monster) tgt;
			if (target.isDead()) {
				continue;
			}

			if (target.getSpoilerId() != 0) {
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_SPOILED));
				continue;
			}

			if (Formulas.calcMagicSuccess(activeChar, (Creature) tgt, skill)) {
				target.setSpoilerId(activeChar.getObjectId());
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SPOIL_SUCCESS));
			} else {
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill.getId()));
			}

			target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
