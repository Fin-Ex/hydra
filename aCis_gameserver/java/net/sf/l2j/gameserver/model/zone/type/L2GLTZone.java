/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 *
 * @author finfan
 */
public final class L2GLTZone extends L2SpawnZone {

	public L2GLTZone(int id) {
		super(id);
	}

	@Override
	public void setParameter(String name, String value) {
		super.setParameter(name, value);
	}

	@Override
	protected void onEnter(Creature character) {
		if(!character.isPlayer()) {
			return;
		}
		
		character.setInsideZone(ZoneId.GLT, true);
	}

	@Override
	protected void onExit(Creature character) {
		if(!character.isPlayer() || !character.getPlayer().isInsideZone(ZoneId.GLT)) {
			return;
		}
		
		character.setInsideZone(ZoneId.GLT, false);
	}

	@Override
	public void onDieInside(Creature character) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onReviveInside(Creature character) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
