package net.sf.l2j.gameserver.skills.conditions;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.base.ClassRace;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public class ConditionPlayerRace extends Condition
{
	private final ClassRace _race;
	
	public ConditionPlayerRace(ClassRace race)
	{
		_race = race;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		if (env.getPlayer() == null)
			return false;
		
		return env.getPlayer().getRace() == _race;
	}
}