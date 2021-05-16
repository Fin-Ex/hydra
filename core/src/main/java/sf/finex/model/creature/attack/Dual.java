/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.creature.attack;

import sf.l2j.commons.concurrent.ThreadPool;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.events.OnDualHit;
import sf.l2j.gameserver.skills.Formulas;

/**
 *
 * @author FinFan
 */
public class Dual extends AbstractHit {

	private final static float[] hitTimeModifier = {
		2.15f,
		1.25f
	};

	public Dual(Creature attacker, Creature target) {
		super(attacker, target);
	}

	@Override
	public boolean start() {
		super.start();

		final DamageInfo[] damageInfo = new DamageInfo[2];
		for (int i = 0; i < damageInfo.length; i++) {
			final DamageInfo info = damageInfo[i] = new DamageInfo();
			info.isMiss = Formulas.calcHitMiss(attacker, target);
			if (!info.isMiss) {
				info.shieldResult = Formulas.calcShldUse(attacker, target, null);
				info.isCrit = Formulas.calcCrit(attacker.getStat().getCriticalHit(target, null));
				info.isParry = Formulas.calcParry(attacker, target, null);
				info.damage = (int) Formulas.calcPhysDam(attacker, target, null, info, attack.soulshot);
				info.damage /= 2;
				attacker.getEventBus().notify(new OnDualHit(attacker, target, info));
			}
		}

		boolean isHit = false;
		for (int i = 0; i < damageInfo.length; i++) {
			final DamageInfo info = damageInfo[i];
			ThreadPool.schedule(new HitTask(this, info, attack.soulshot), (long) (hitTime / hitTimeModifier[i]));
			attack.hit(attack.createHit(target, info));
			isHit |= !info.isMiss;
		}

		return isHit;
	}
}
