/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.creature.attack;

import org.slf4j.LoggerFactory;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.events.OnSimpleHit;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 *
 * @author FinFan
 */
public class Simple extends AbstractHit {

	public Simple(Creature attacker, Creature target) {
		super(attacker, target);
	}

	@Override
	public boolean start() {
		super.start();

		final DamageInfo info = new DamageInfo();
		info.isMiss = Formulas.calcHitMiss(attacker, target);
		if(!info.isMiss) {
			info.shieldResult = Formulas.calcShldUse(attacker, target, null);
			info.isParry = Formulas.calcParry(attacker, target, null);
			info.isCrit = Formulas.calcCrit(attacker.getStat().getCriticalHit(target, null));
			info.damage = (int) Formulas.calcPhysDam(attacker, target, null, info, attack.soulshot);
//			if (attackpercent != 100) {
//				damage1 = (int) (damage1 * attackpercent / 100);
//			}
			attacker.getEventBus().notify(new OnSimpleHit(attacker, target, info));
		}

		// Create a new hit task with Medium priority
		ThreadPool.schedule(new HitTask(this, info, attack.soulshot), hitTime);

		// Add this hit to the Server-Client packet Attack
		attack.hit(attack.createHit(target, info));

		// Return true if hit isn't missed
		return !info.isMiss;
	}
}
