package net.sf.l2j.gameserver.handler;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public interface ISkillHandler
{
	public static Logger _log = LoggerFactory.getLogger(ISkillHandler.class.getName());
	
	/**
	 * this is the worker method that is called when using a skill.
	 * @param activeChar The Creature who uses that skill.
	 * @param skill The skill object itself.
	 * @param targets Eventual targets.
	 */
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets);
	
	/**
	 * this method is called at initialization to register all the skill ids automatically
	 * @return all known itemIds
	 */
	public ESkillType[] getSkillIds();
}