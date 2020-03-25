/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import lombok.Getter;
import net.sf.finex.enums.EMovieAction;
import net.sf.finex.model.movie.MessageData;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author finfan
 */
public abstract class AbstractActorAction {

	protected static final Logger log = LoggerFactory.getLogger(AbstractActorAction.class);
	
	@Getter protected final Creature actor;
	@Getter protected final WorldObject target;
	protected MessageData message;
	@Getter protected StatsSet stats;
	protected String sound;

	public AbstractActorAction(Creature actor, WorldObject target) {
		this.actor = actor;
		this.target = target;
		this.stats = new StatsSet();
	}

	public AbstractActorAction(Creature actor) {
		this(actor, null);
	}
	
	public AbstractActorAction() {
		this(null, null);
	}
	
	public final AbstractActorAction setMessage(MessageData message) {
		this.message = message;
		return this;
	}

	protected void say() {
		try {
			if (message != null) {
				String text = message.getMessage();
				if(actor != null) {
					text = text.replace("%selfName%", actor.getName());
				}

				if(target != null) {
					text = text.replace("%targetName%", target.getName());
				}
				if (actor.isPlayer()) {
					actor.getPlayer().broadcastSay(message.getSay2(), text);
				} else {
					actor.getNpc().broadcastNpcSay(text);
				}
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException("Trying to send message not by NPC or Player.", e);
		}
	}

	protected void abortActorGameplayActions(boolean stopEffects) {
		actor.stopMove(null);
		actor.abortAttack();
		actor.abortCast();
		if (stopEffects) {
			actor.stopAllEffects();
		}
	}

	public void setVisible() {
		if(actor != null && actor.isVisible()) {
			actor.setIsVisible(true);
			actor.setXYZ(actor.getX(), actor.getY(), actor.getZ());
		}
	}
	
	public abstract void call();

	public abstract EMovieAction getType();

	public abstract long getTiming();
}
