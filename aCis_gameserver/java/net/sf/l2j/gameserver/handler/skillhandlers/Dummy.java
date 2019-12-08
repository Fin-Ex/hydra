package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class Dummy implements ISkillHandler
{
	private static final ESkillType[] SKILL_IDS =
	{
		ESkillType.DUMMY,
		ESkillType.BEAST_FEED,
		ESkillType.DELUXE_KEY_UNLOCK
	};
	
	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets)
	{
		if (!(activeChar instanceof Player))
			return;
		
		if (skill.getSkillType() == ESkillType.BEAST_FEED)
		{
			final WorldObject target = targets[0];
			if (target == null)
				return;
		}
	}
	
	@Override
	public ESkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}