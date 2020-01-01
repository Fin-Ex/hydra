package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * Mobs can teleport players to them.
 */
public class GetPlayer implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.GET_PLAYER
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (activeChar.isAlikeDead()) {
			return;
		}

		for (WorldObject target : targets) {
			final Player victim = target.getPlayer();
			if (victim == null || victim.isAlikeDead()) {
				continue;
			}

			victim.teleToLocation(activeChar.getX(), activeChar.getY(), activeChar.getZ(), 0);
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
