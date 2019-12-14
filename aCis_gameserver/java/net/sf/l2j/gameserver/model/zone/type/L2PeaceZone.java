package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 * A peaceful zone
 *
 * @author durgus
 */
public class L2PeaceZone extends L2ZoneType {

	public L2PeaceZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		character.setInsideZone(ZoneId.PEACE, true);
	}

	@Override
	protected void onExit(Creature character) {
		character.setInsideZone(ZoneId.PEACE, false);
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}
}
