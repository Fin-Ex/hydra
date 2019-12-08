package net.sf.l2j.gameserver.model.actor.events;


import lombok.Data;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * OnSuccessParry
 *
 * @author zcxv
 * @date 19.05.2019
 */
@Data
public class OnSuccessParry {
	
	private final Creature self;
	private final Creature attacker;
	private final L2Skill parriedSkill;
	
}
