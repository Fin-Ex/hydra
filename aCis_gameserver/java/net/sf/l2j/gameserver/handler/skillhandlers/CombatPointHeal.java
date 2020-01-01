package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class CombatPointHeal implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.COMBATPOINTHEAL
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		// check for other effects
		final IHandler handler = HandlerTable.getInstance().get(Continuous.class);
		if (handler != null) {
			handler.invoke(activeChar, skill, targets);
		}

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = (Creature) obj;
			if (target.isDead() || target.isInvul()) {
				continue;
			}

			double cp = skill.getPower();

			if ((target.getCurrentCp() + cp) >= target.getMaxCp()) {
				cp = target.getMaxCp() - target.getCurrentCp();
			}

			target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CP_WILL_BE_RESTORED).addNumber((int) cp));
			target.setCurrentCp(cp + target.getCurrentCp());

			StatusUpdate sump = new StatusUpdate(target);
			sump.addAttribute(StatusUpdate.CUR_CP, (int) target.getCurrentCp());
			target.sendPacket(sump);
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
