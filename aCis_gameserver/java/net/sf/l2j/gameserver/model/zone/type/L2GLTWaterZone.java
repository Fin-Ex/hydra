/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.model.actor.Creature;

/**
 *
 * @author finfan
 */
public class L2GLTWaterZone extends L2GLTZone {
	
	public L2GLTWaterZone(int id) {
		super(id);
	}

	@Override
	protected void onExit(Creature character) {
		super.onExit(character);
	}

	@Override
	protected void onEnter(Creature character) {
		super.onEnter(character);
	}
}
