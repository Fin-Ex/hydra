package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 * An artifact's castle zone
 *
 * @author Tryskell
 */
public class L2PrayerZone extends L2ZoneType {

	public L2PrayerZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		character.setInsideZone(ZoneId.CAST_ON_ARTIFACT, true);
	}

	@Override
	protected void onExit(Creature character) {
		character.setInsideZone(ZoneId.CAST_ON_ARTIFACT, false);
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}
}
