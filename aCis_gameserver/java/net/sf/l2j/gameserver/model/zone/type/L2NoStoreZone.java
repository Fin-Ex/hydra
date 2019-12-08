package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 * Zone where store is not allowed.
 * @author fordfrog
 */
public class L2NoStoreZone extends L2ZoneType
{
	public L2NoStoreZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final Creature character)
	{
		if (character instanceof Player)
			character.setInsideZone(ZoneId.NO_STORE, true);
	}
	
	@Override
	protected void onExit(final Creature character)
	{
		if (character instanceof Player)
			character.setInsideZone(ZoneId.NO_STORE, false);
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