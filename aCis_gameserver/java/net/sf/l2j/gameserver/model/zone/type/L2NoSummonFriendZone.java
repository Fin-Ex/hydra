package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 * A simple no summon zone
 *
 * @author JIV
 */
public class L2NoSummonFriendZone extends L2ZoneType {

	public L2NoSummonFriendZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
	}

	@Override
	protected void onExit(Creature character) {
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}
}
