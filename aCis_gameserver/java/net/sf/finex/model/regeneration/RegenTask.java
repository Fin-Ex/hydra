/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.regeneration;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 *
 * @author FinFan
 */
public class RegenTask implements Runnable {

	private final Creature owner;
	private final ERegenType type;

	public RegenTask(Creature owner, ERegenType type) {
		this.owner = owner;
		this.type = type;
	}

	@Override
	public void run() {
		switch (type) {
			case HP:
				owner.setCurrentHp(owner.getCurrentHp() + Formulas.calcHpRegen(owner));
				break;

			case CP:
				owner.setCurrentCp(owner.getCurrentCp() + Formulas.calcCpRegen(owner.getPlayer()));
				break;

			case MP:
				owner.setCurrentMp(owner.getCurrentMp() + Formulas.calcMpRegen(owner));
				break;
		}

		owner.broadcastStatusUpdate();
	}
}
