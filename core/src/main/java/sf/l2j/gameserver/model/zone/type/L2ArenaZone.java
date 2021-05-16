package sf.l2j.gameserver.model.zone.type;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.zone.L2SpawnZone;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.network.SystemMessageId;

/**
 * An arena
 *
 * @author durgus
 */
public class L2ArenaZone extends L2SpawnZone {

	public L2ArenaZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		if (character instanceof Player) {
			if (!character.isInsideZone(ZoneId.PVP)) {
				((Player) character).sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
			}
		}

		character.setInsideZone(ZoneId.PVP, true);
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
	}

	@Override
	protected void onExit(Creature character) {
		character.setInsideZone(ZoneId.PVP, false);
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);

		if (character instanceof Player) {
			if (!character.isInsideZone(ZoneId.PVP)) {
				((Player) character).sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
			}
		}
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}
}
