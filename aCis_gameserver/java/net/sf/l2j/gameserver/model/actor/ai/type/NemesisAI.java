/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.ai.type;

import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Nemesis;
import net.sf.l2j.gameserver.model.location.Location;

/**
 *
 * @author finfan
 */
public class NemesisAI extends AttackableAI {

	public static final String[] QUOTES = {
		" DIE...",
		" must die...",
		" must be dead...",
		" must be killed..."
	};
	
	public NemesisAI(Attackable attackable) {
		super(attackable);
	}

	@Override
	protected void thinkAttack() {
		final Attackable npc = getActor();
		if (npc.isCastingNow()) {
			return;
		}

		// Pickup most hated character.
		final Creature attackTarget = npc.getMostHated();
		setTarget(attackTarget);
		npc.setTarget(attackTarget);

		final int actorCollision = (int) npc.getCollisionRadius();
		final int combinedCollision = (int) (actorCollision + attackTarget.getCollisionRadius());
		final double dist = Math.sqrt(npc.getPlanDistanceSq(attackTarget.getX(), attackTarget.getY()));

		int range = combinedCollision;
		if (attackTarget.isMoving()) {
			range += 15;
		}

		if (npc.isMoving()) {
			range += 15;
		}

		if (MathUtil.calculateDistance(npc, attackTarget, true) > 2700) {
			npc.teleToLocation(attackTarget.getPosition(), 200);
			npc.broadcastNpcSay(attackTarget.getName() + getQuote());
		} else if (dist > range || !GeoEngine.getInstance().canSeeTarget(npc, attackTarget)) {
			
			if (Rnd.calcChance(3, 100)) {
				npc.broadcastNpcSay(attackTarget.getName() + getQuote());
			}
			
			if (attackTarget.isMoving()) {
				range -= 30;
			}

			if (range < 5) {
				range = 5;
			}

			moveToPawn(attackTarget, range);
			return;
		}
		
		_actor.doAttack((Creature) getTarget());
	}

	public static final String getQuote() {
		return Rnd.get(QUOTES);
	}
	
	@Override
	public Nemesis getActor() {
		return (Nemesis) super.getActor();
	}
}
