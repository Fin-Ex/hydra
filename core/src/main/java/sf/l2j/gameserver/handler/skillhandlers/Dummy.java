package sf.l2j.gameserver.handler.skillhandlers;

import sf.l2j.gameserver.handler.IHandler;

import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.ESkillType;

public class Dummy implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.DUMMY,
		ESkillType.BEAST_FEED,
		ESkillType.DELUXE_KEY_UNLOCK
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		if (skill.getSkillType() == ESkillType.BEAST_FEED) {
			final WorldObject target = targets[0];
			if (target == null) {
				return;
			}
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
