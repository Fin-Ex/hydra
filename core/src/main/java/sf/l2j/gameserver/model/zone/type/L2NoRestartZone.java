package sf.l2j.gameserver.model.zone.type;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.zone.L2ZoneType;
import sf.l2j.gameserver.model.zone.ZoneId;

/**
 * Zone where restart is not allowed.<BR>
 * This zone will be rworked later (need to burn GrandBossManager), atm it's a
 * simple zone.
 *
 * @author Tryskell
 */
public class L2NoRestartZone extends L2ZoneType {

	public L2NoRestartZone(final int id) {
		super(id);
	}

	@Override
	protected void onEnter(final Creature character) {
		if (character instanceof Player) {
			character.setInsideZone(ZoneId.NO_RESTART, true);
		}
	}

	@Override
	protected void onExit(final Creature character) {
		if (character instanceof Player) {
			character.setInsideZone(ZoneId.NO_RESTART, false);
		}
	}

	@Override
	public void onDieInside(final Creature character) {
	}

	@Override
	public void onReviveInside(final Creature character) {
	}
}
