/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.creature.attack;

import sf.l2j.commons.concurrent.ThreadPool;
import sf.l2j.commons.math.MathUtil;
import sf.l2j.gameserver.model.ShotType;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.events.OnMassHit;
import sf.l2j.gameserver.network.serverpackets.Attack;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.Stats;

/**
 *
 * @author FinFan
 */
public class Mass extends Simple {

	private final int maxRadius;
	private final int maxAngleDiff;
	private final int attackRandomCountMax;
	private int atkPercent;

	public Mass(Creature attacker, Creature target) {
		super(attacker, target);
		maxRadius = attacker.getPhysicalAttackRange();
		maxAngleDiff = (int) attacker.getStat().calcStat(Stats.PAtkAngle, 120, null, null);
		atkPercent = (int) attacker.getStat().calcStat(Stats.PoleAtkPercent, 85, null, null);
		attackRandomCountMax = (int) attacker.getStat().calcStat(Stats.AtkCountMax, 0, null, null) - 1;
		// Recharge any active auto soulshot tasks for current Creature instance.
		attacker.rechargeShots(true, false);
		hitTime = Formulas.calcPAtkSpd(attacker, target, attacker.getPAtkSpd());
		attacker.setAttackEndTime(System.currentTimeMillis() + hitTime);
		attack = new Attack(attacker, attacker.isChargedShot(ShotType.SOULSHOT), (weapon != null) ? weapon.getCrystalType().getId() : 0);
		attacker.setHeading(MathUtil.calculateHeadingFrom(attacker, target));
	}

	@Override
	public boolean start() {
		// Get the number of targets (-1 because the main target is already used)
		int attackcount = 0;

		boolean hitted = doAttackHitByPole(target);

		for (Creature potentialTarget : attacker.getKnownType(Creature.class)) {
			if (potentialTarget == target || potentialTarget.isAlikeDead()) {
				continue;
			}

			if (attacker.isPlayer()) {
				if (potentialTarget.isPet() && potentialTarget.getPet().getPlayer() == attacker) {
					continue;
				}
			} else if (attacker.isAttackableInstance()) {
				if (potentialTarget.isPlayer() && getTarget().isAttackableInstance()) {
					continue;
				}

				if (potentialTarget.isAttackableInstance() && !attacker.isConfused()) {
					continue;
				}
			}

			if (!MathUtil.checkIfInShortRadius(maxRadius, attacker, potentialTarget, false)) {
				continue;
			}

			// otherwise hit too high/low. 650 because mob z coord sometimes wrong on hills
			if (Math.abs(potentialTarget.getZ() - attacker.getZ()) > 650) {
				continue;
			}

			if (!attacker.isFacing(potentialTarget, maxAngleDiff)) {
				continue;
			}

			// Launch an attack on each character, until attackRandomCountMax is reached.
			if (potentialTarget == attacker.getAI().getTarget() || potentialTarget.isAutoAttackable(attacker)) {
				attackcount++;
				if (attackcount > attackRandomCountMax) {
					break;
				}

				hitted |= doAttackHitByPole(potentialTarget);
				atkPercent /= 1.15;
			}
		}
		// Return true if one hit isn't missed
		return hitted;
	}

	private boolean doAttackHitByPole(Creature newTarget) {
		DamageInfo info = new DamageInfo();
		info.isMiss = Formulas.calcHitMiss(attacker, newTarget);

		// Check if hit isn't missed
		if (!info.isMiss) {
			info.shieldResult = Formulas.calcShldUse(attacker, newTarget, null);
			info.isParry = Formulas.calcParry(attacker, newTarget, null);
			info.isCrit = Formulas.calcCrit(attacker.getStat().getCriticalHit(newTarget, null));
			info.damage = (int) Formulas.calcPhysDam(attacker, newTarget, null, info, attack.soulshot) * atkPercent / 100;
			attacker.getEventBus().notify(new OnMassHit(attacker, newTarget, info));
		}

		// Create a new hit task with Medium priority
		ThreadPool.schedule(new HitTask(this, info, attack.soulshot), hitTime);

		// Add this hit to the Server-Client packet Attack
		attack.hit(attack.createHit(newTarget, info));

		// Return true if hit isn't missed
		return !info.isMiss;
	}
}
