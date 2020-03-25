/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.network.serverpackets.Attack;
import net.sf.l2j.gameserver.network.serverpackets.AutoAttackStop;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 *
 * @author finfan
 */
public class ActAttack extends AbstractActorAction {

	private DamageInfo info;
	private int ss;

	public ActAttack(Creature actor, WorldObject target) {
		super(actor, target);
	}

	@Override
	public void call() {
		say();
		try {
			actor.setTarget(target);
			final Attack attack = new Attack(actor, ss > 0, ss);
			attack.hit(attack.createHit(target, info));
			actor.broadcastPacket(attack);
			if (info != null && info.damage > 0 && target.isCreature()) {
				ThreadPool.schedule(() -> {
					final Creature creature = target.getCreature();
					creature.getStatus().setCurrentHp(Math.max(creature.getCurrentHp() - info.damage, 0));

					// Die if character is mortal
					if (creature.getCurrentHp() < 0.5) {
						creature.abortAttack();
						creature.abortCast();
						creature.doDie(actor);
					}
				}, getTiming());
			}
			actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.ATTACK;
	}

	public ActAttack setSs(int ss) {
		this.ss = ss;
		return this;
	}

	public ActAttack setInfo(DamageInfo info) {
		this.info = info;
		return this;
	}

	@Override
	public long getTiming() {
		final int time = (actor.getAttackType() == WeaponType.BOW ? 1500 * 345 / actor.getPAtkSpd()
				: Formulas.calcPAtkSpd(actor, target.getCreature(), actor.getPAtkSpd()));
		return time;
	}
}
