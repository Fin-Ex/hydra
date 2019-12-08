/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.creature.attack;


import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.events.OnBowHit;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.Stats;

/**
 *
 * @author FinFan
 */
public class Bow extends AbstractHit {

	public Bow(Creature attacker, Creature target) {
		super(attacker, target);
	}

	@Override
	public boolean start() {
		super.start();

		DamageInfo info = new DamageInfo();
		info.isMiss = Formulas.calcHitMiss(attacker, target);
		
		// Consume arrows
		attacker.reduceArrowCount();

		if (!info.isMiss) {
			info.shieldResult = Formulas.calcShldUse(attacker, target, null);
			info.isParry = Formulas.calcParry(attacker, target, null);
			info.isCrit = Formulas.calcCrit(attacker.getStat().getCriticalHit(target, null));
			info.damage = (int) Formulas.calcPhysDam(attacker, target, null, info, attack.soulshot);
			attacker.getEventBus().notify(new OnBowHit(attacker, target, info));
		}

		int arrowReloadTime = (int) attacker.calcStat(Stats.ArrowReloadSpd, weapon.getReuseDelay(), target, null);
		if (arrowReloadTime != 0) {
			arrowReloadTime = (arrowReloadTime * 345) / attacker.getStat().getPAtkSpd();
		}

		if (attacker.isPlayer()) {
			attacker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.GETTING_READY_TO_SHOOT_AN_ARROW));
			attacker.sendPacket(new SetupGauge(SetupGauge.GaugeColor.RED, hitTime + arrowReloadTime));
		}

		ThreadPool.schedule(new HitTask(this, info, attack.soulshot), hitTime);
		attacker.setDisableBowAttackEndTime(System.currentTimeMillis() + (hitTime + arrowReloadTime));
		attack.hit(attack.createHit(target, info));
		return !info.isMiss;
	}
}
