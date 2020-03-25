/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.gameserver.model.actor.Creature;

/**
 *
 * @author finfan
 */
public class ActRunnableAction extends AbstractActorAction {

	private final Runnable code;

	public ActRunnableAction(Runnable code) {
		this.code = code;
	}

	@Override
	public void call() {
		code.run();
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.DO_SOMETHING;
	}

	@Override
	public long getTiming() {
		return 1000;
	}
}
