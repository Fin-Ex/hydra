/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import lombok.Getter;
import net.sf.finex.enums.EMovieAction;
import net.sf.finex.enums.ESocialAction;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.GetItem;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

/**
 *
 * @author finfan
 */
public class ActSocial extends AbstractActorAction {

	@Getter private final ESocialAction social;

	public ActSocial(ESocialAction social, Creature actor) {
		super(actor);
		this.social = social;
	}

	@Override
	public void call() {
		abortActorGameplayActions(false);
		say();
		if (actor.isPlayer()) {
			if(social == null) {
				actor.broadcastPacket(new SocialAction(actor, social.getId()));
			} else switch (social) {
				case Pickup:
					actor.broadcastPacket(new GetItem(actor.getPlayable()));
					break;
					
				case FakeDeath:
					actor.startFakeDeath();
					break;
					
				case StandUp:
					if(actor.isAlikeDead()) {
						actor.stopFakeDeath(true);
					}
					actor.getPlayer().forceStandUp();
					break;
					
				default:
					actor.broadcastPacket(new SocialAction(actor, social.getId()));
					break;
			}
		} else if (actor.isNpc()) {
			actor.getNpc().broadcastPacket(new SocialAction(actor, 2));
		} else {
			throw new UnsupportedOperationException("No social action for non NPC/Player character.");
		}
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.SOCIAL;
	}

	@Override
	public long getTiming() {
		return social == ESocialAction.Pickup ? 700 : 2500;
	}

}
