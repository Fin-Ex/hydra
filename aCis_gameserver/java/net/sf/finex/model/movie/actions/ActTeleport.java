/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import lombok.Getter;
import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

/**
 *
 * @author finfan
 */
public class ActTeleport extends AbstractActorAction {

	@Getter private final Location teleportLocation;
	private boolean withEffect;
	private int offset;

	public ActTeleport(Location loc, Creature actor) {
		super(actor);
		this.teleportLocation = loc;
	}

	public ActTeleport(Creature owner, int x, int y, int z) {
		this(new Location(x, y, z), owner);
	}

	@Override
	public void call() {
		abortActorGameplayActions(false);
		if(withEffect) {
			actor.broadcastPacket(new MagicSkillUse(actor, 2021, 1, 0, 0));
		}
		say();
		teleportLocation.set(actor.teleToLocation(teleportLocation, offset));
	}

	public ActTeleport setWithEffect() {
		this.withEffect = true;
		return this;
	}

	public ActTeleport setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.TELEPORT;
	}

	@Override
	public long getTiming() {
		return 3000;
	}
	
}
