package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

/**
 * A fishing zone
 *
 * @author durgus
 */
public class L2FishingZone extends L2ZoneType {

	public L2FishingZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
	}

	@Override
	protected void onExit(Creature character) {
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}

	/*
	 * getWaterZ() this added function returns the Z value for the water surface. In effect this simply returns the upper Z value of the zone. This required some modification of L2ZoneForm, and zone form extentions.
	 */
	public int getWaterZ() {
		return getForm().getHighZ();
	}
}
