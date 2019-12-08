package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 * The only zone where 'Build Headquarters' is allowed.
 * @author Tryskell, reverted version of Gnat's NoHqZone
 */
public class L2HqZone extends L2ZoneType
{
	public L2HqZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final Creature character)
	{
		if (character instanceof Player)
			character.setInsideZone(ZoneId.HQ, true);
	}
	
	@Override
	protected void onExit(final Creature character)
	{
		if (character instanceof Player)
			character.setInsideZone(ZoneId.HQ, false);
	}
	
	@Override
	public void onDieInside(final Creature character)
	{
	}
	
	@Override
	public void onReviveInside(final Creature character)
	{
	}
}