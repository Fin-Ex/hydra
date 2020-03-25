/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

/**
 *
 * @author finfan
 */
public class ActCast extends AbstractActorAction {

	private final int id, level;
	private boolean simulate;

	public ActCast(int id, int level, Creature actor, WorldObject target) {
		super(actor, target);
		this.id = id;
		this.level = level;
	}

	@Override
	public void call() {
		abortActorGameplayActions(false);
		say();
		try {
			if (simulate) {
				if (target != null) {
					actor.broadcastPacket(new MagicSkillUse(actor, target.getCreature(), id, level, 2000, 0));
				} else {
					actor.broadcastPacket(new MagicSkillUse(actor, id, level, 2000, 0));
				}
			} else {
				actor.setTarget(target == null ? actor : target);
				actor.doCast(SkillTable.getInstance().getInfo(id, level));
			}
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.CAST;
	}

	public ActCast setSimulate() {
		this.simulate = true;
		return this;
	}

	@Override
	public long getTiming() {
		return simulate ? 2000 : SkillTable.getInstance().getInfo(id, level).getHitTime() + 400;
	}

}
