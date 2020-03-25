/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author finfan
 */
public class ActMount extends AbstractActorAction {

	private final int mountId;

	public ActMount(int mountId, Creature actor) {
		super(actor);
		this.mountId = mountId;
	}
	
	@Override
	public void call() {
		if(!actor.isPlayer()) {
			log.warn("ActMount: actor can't be a NON player instance for mounting on a pet.");
			return;
		}
		
		final Player actorPlayer = actor.getPlayer();
		if (mountId == 0) {
			actorPlayer.dismount();
		} else {
			if (actorPlayer.isMounted()) {
				actorPlayer.dismount();
			}

			if (actorPlayer.hasPet() || actorPlayer.hasServitor()) {
				actorPlayer.getActiveSummon().unSummon(actorPlayer);
			}

			actorPlayer.mount(mountId, 0, false);
		}
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.MOUNT;
	}

	@Override
	public long getTiming() {
		return 1888;
	}
}
