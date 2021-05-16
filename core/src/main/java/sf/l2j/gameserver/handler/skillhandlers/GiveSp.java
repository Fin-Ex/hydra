package sf.l2j.gameserver.handler.skillhandlers;

import sf.l2j.gameserver.handler.IHandler;

import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author Forsaiken
 */
public class GiveSp implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.GIVE_SP
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		for (WorldObject obj : targets) {
			Creature target = (Creature) obj;
			if (target != null) {
				int spToAdd = (int) skill.getPower();
				target.addExpAndSp(0, spToAdd);
			}
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
