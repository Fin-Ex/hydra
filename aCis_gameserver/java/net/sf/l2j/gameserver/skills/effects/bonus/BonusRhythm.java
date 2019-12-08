/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.skills.effects.bonus;

import net.sf.finex.model.classes.Bladedancer;
import net.sf.l2j.gameserver.model.actor.Creature;

/**
 *
 * @author FinFan
 */
public class BonusRhythm implements IBonusHandler {

	@Override
	public double calc(Creature creature) {
		if(!creature.isPlayer()) {
			return 1;
		}
		
		final Bladedancer bd = creature.getPlayer().getComponent(Bladedancer.class);
		if(bd != null) {
			return bd.calcRhythm();
		}
		return 1;
	}
	
}
