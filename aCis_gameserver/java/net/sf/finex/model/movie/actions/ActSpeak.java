/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import net.sf.finex.enums.EMovieAction;
import net.sf.finex.enums.ESocialAction;
import net.sf.finex.model.movie.MessageData;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

/**
 *
 * @author finfan
 */
public class ActSpeak extends AbstractActorAction {

	private ESpeechType speechType;
	
	public ActSpeak(Creature actor, MessageData message) {
		super(actor);
		setMessage(message);
	}

	@Override
	public void call() {
		if(speechType != null) {
			if (actor.isNpc()) {
				actor.broadcastPacket(new SocialAction(actor, speechType.getSocialID(true)));
			} else if (actor.isPlayer()) {
				actor.getPlayer().broadcastPacket(new SocialAction(actor, speechType.getSocialID(false)));
			}
		}
		
		say();
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.SPEAK;
	}

	public ActSpeak setSpeechType(ESpeechType speechType) {
		this.speechType = speechType;
		return this;
	}

	@Override
	public long getTiming() {
		final int length = message.getMessage().length();
		float modifier = length / 100.f + 1.f;
		return (long) (500 + (length * 150 / modifier));
	}
	
	public enum ESpeechType {
		NORMAL,
		GLORY(ESocialAction.Victory),
		THINKING(ESocialAction.Nod);
		
		private final ESocialAction social;

		private ESpeechType(ESocialAction social) {
			this.social = social;
		}

		private ESpeechType() {
			this.social = null;
		}
		
		public int getSocialID(boolean isNpc) {
			if(this == NORMAL) {
				return 1;
			}
			
			return isNpc ? Rnd.get(2, 3) : social.getId();
		}
	}
}
