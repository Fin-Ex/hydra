/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.finex.model.movie.MovieManager;
import net.sf.l2j.gameserver.model.actor.Creature;

/**
 *
 * @author finfan
 */
public class ActDeleteNPC extends AbstractActorAction {

	private final MovieManager manager;

	public ActDeleteNPC(MovieManager manager, Creature actor) {
		super(actor);
		this.manager = manager;
	}
	
	@Override
	public void call() {
		assert actor.isPlayer();
		actor.stopMove(null);
		actor.abortAttack();
		actor.abortCast();
		actor.deleteMe();
		manager.removeActor(actor);
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.DELETE_NPC;
	}

	@Override
	public long getTiming() {
		return 0;
	}
	
}
