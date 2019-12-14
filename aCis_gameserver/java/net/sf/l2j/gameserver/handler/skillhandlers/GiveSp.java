package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author Forsaiken
 */
public class GiveSp implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS
			= {
				ESkillType.GIVE_SP
			};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
		for (WorldObject obj : targets) {
			Creature target = (Creature) obj;
			if (target != null) {
				int spToAdd = (int) skill.getPower();
				target.addExpAndSp(0, spToAdd);
			}
		}
	}

	@Override
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
