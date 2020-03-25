/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.StartRotation;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;

/**
 *
 * @author finfan
 */
public class ActRotate extends AbstractActorAction {

	private final ERotateType rotateType;
	private long timing;
	
	public ActRotate(ERotateType rotateType, Creature actor, WorldObject target) {
		super(actor, target);
		this.rotateType = rotateType;
	}

	public ActRotate(ERotateType rotateType, Creature actor) {
		super(actor);
		this.rotateType = rotateType;
	}

	@Override
	public void call() {
		int heading = 0;
		switch (rotateType) {
			case TO_TARGET:
				heading = MathUtil.calculateHeadingFrom(actor, target);
				actor.broadcastPacket(new StartRotation(actor.getObjectId(), target.getCreature().getHeading()));
				actor.broadcastPacket(new StopRotation(actor.getObjectId(), heading));
				break;
				
			case TO_DEFAULT:
				actor.broadcastPacket(new StartRotation(actor.getObjectId(), Short.MAX_VALUE));
				actor.broadcastPacket(new StopRotation(actor.getObjectId(), heading));
				break;
				
			case ARROUND:
				actor.broadcastPacket(new StartRotation(actor.getObjectId(), Short.MAX_VALUE));
				actor.broadcastPacket(new StopRotation(actor.getObjectId(), actor.getHeading()));
				break;
				
			case BACK_TO_TARGET:
				heading = target.getCreature().getHeading();
				actor.broadcastPacket(new StartRotation(actor.getObjectId(), actor.getHeading()));
				actor.broadcastPacket(new StopRotation(actor.getObjectId(), heading));
				break;
				
			case HALF:
				int degree = (int) MathUtil.convertHeadingToDegree(actor.getHeading());
				heading = MathUtil.convertDegreeToClientHeading(degree / 2);
				actor.broadcastPacket(new StartRotation(actor.getObjectId(), actor.getHeading()));
				actor.broadcastPacket(new StopRotation(actor.getObjectId(), heading));
				break;
				
			default:
				log.warn("{} not inplemented.", rotateType);
				break;
		}
		actor.setHeading(heading);
		say();
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.ROTATE;
	}

	public ActRotate setTiming(long timing) {
		this.timing = timing;
		return this;
	}

	@Override
	public long getTiming() {
		return timing;
	}

	public enum ERotateType {
		TO_DEFAULT,
		TO_TARGET,
		ARROUND,
		BACK_TO_TARGET,
		TO_RIGHT,
		TO_LEFT,
		HALF
	}
}
