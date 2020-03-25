/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import lombok.extern.slf4j.Slf4j;
import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 *
 * @author finfan
 */
@Slf4j
@Deprecated
public class ActTransform extends AbstractActorAction {

	private final Npc target;
	private final int transformId;
	private boolean realTransform;
	private String message;
	private int time;
	
	public ActTransform(Npc target, int transformId) {
		this.target = target;
		this.transformId = transformId;
	}

	@Override
	public void call() {
		// spawn new npc wich ID is transforming...
		try {
			final NpcTemplate template = NpcTable.getInstance().getTemplate(transformId);
			target.setFakeTemplate(template);
			if(message != null) {
				target.broadcastNpcSay(message);
			}
			target.getKnownType(Player.class).forEach(pc -> pc.refreshInfos());
		} catch (Exception e) {
			log.error("Error when trying to set fake template to {}", target.getNpcId(), e);
		}
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.TRANSFORM;
	}

	public ActTransform setMessage(String message) {
		this.message = message;
		return this;
	}

	public ActTransform setRealTransform(boolean realTransform) {
		this.realTransform = realTransform;
		return this;
	}

	@Override
	public long getTiming() {
		return time;
	}
	
}
