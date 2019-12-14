package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * Mobs can teleport players to them.
 */
public class GetPlayer implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS
			= {
				ESkillType.GET_PLAYER
			};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
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
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
