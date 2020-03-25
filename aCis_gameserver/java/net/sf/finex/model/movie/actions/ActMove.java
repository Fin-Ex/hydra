/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.location.Location;

/**
 *
 * @author finfan
 */
public class ActMove extends AbstractActorAction {

	private final Location routePoint;
	private boolean walk;
	private boolean near;

	public ActMove(Location routePoint, Creature actor) {
		super(actor);
		this.routePoint = routePoint;
	}
	
	public ActMove(Location routePoint, Creature actor, WorldObject target) {
		super(actor, target);
		this.routePoint = routePoint;
	}
	
	@Override
	public void call() {
		abortActorGameplayActions(false);
		
		setVisible();
		
		if (walk) {
			actor.setWalking();
		} else {
			actor.setRunning();
		}
		
		say();
		
		if (near) {
			routePoint.add(Rnd.get(20), Rnd.get(20), 0);
			actor.getAI().setIntention(CtrlIntention.MOVE_TO, routePoint);
		} else {
			actor.getAI().setIntention(CtrlIntention.MOVE_TO, routePoint);
		}
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.MOVE;
	}

	public ActMove setWalk() {
		this.walk = true;
		return this;
	}
	
	public ActMove setNear() {
		this.near = true;
		return this;
	}
	
	@Override
	public long getTiming() {
		double distance = MathUtil.calculateDistance(actor.getPosition().getX(), actor.getPosition().getY(), actor.getPosition().getZ(), routePoint.getX(), routePoint.getY());
		long time = (long) (distance / actor.getMoveSpeed() * 1000);
		return time;
	}
}
