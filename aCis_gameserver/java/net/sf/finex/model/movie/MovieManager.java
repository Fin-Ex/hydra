/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 *
 * @author finfan
 */
@Slf4j
public class MovieManager {

	@Getter private final List<Creature> actors;
	@Getter private final Queue<ActionData> actions;
	@Getter private final StatsSet stats;
	@Getter private int actionId;
	@Setter private long totalTiming;
	
	public MovieManager() {
		actors = new CopyOnWriteArrayList<>();
		actions = new ArrayDeque<>();
		stats = new StatsSet();
	}
	
	public void addActor(Creature actor) {
		if(actors.contains(actor)) {
			return;
		}
		
		if(actor.isPlayer()) {
			if(actor.getPlayer().isSitting()) {
				actor.getPlayer().forceStandUp();
			} else if(actor.isAlikeDead()) {
				actor.stopFakeDeath(true);
			}
		}
		
		actors.add(actor);
		actor.setOnMovie(true);
	}
	
	public void removeActor(Creature actor) {
		actors.remove(actor);
		actor.setOnMovie(false);
	}
	
	public void addAction(ActionData data) {
		data.setId(actionId++);
		actions.add(data);
		totalTiming += data.getAction().getTiming();
		if (data.getAction() != null && data.getAction().getActor() != null) {
			addActor(data.getAction().getActor());
		}
	}
	
	public <T> void set(String name, T t) {
		stats.set(name, t);
	}
	
	public <T> T get(String name) {
		return (T) stats.get(name);
	}
	
	public Creature getRandomActor() {
		return Rnd.get(actors);
	}
	
	public void startMovie() {
		log.info("MovieManager: movie timing is {}", TimeUnit.MILLISECONDS.toMinutes(totalTiming));
		try {
			//actions.sort(Comparator.comparing(ActionData::getId));
			while (!actions.isEmpty()) {
				final ActionData data = actions.poll();
				data.getAction().call();
				if (data.getAction().getTiming() > 0) {
					synchronized (actions) {
						actions.wait(data.getAction().getTiming());
					}
				}
			}
			cutMovie();
		} catch (InterruptedException ex) {
			log.error("", ex);
		}
	}
	
	public void cutMovie() {
		actors.forEach(creature -> removeActor(creature));
		actors.clear();
		actions.clear();
		stats.clear();
	}	
	
	public int getTotalTimingIn(TimeUnit tu) {
		switch(tu) {
			case SECONDS:
				return (int) TimeUnit.MILLISECONDS.toSeconds(totalTiming);
			case MINUTES:
				return (int) TimeUnit.MILLISECONDS.toMinutes(totalTiming);
			case HOURS:
				return (int) TimeUnit.MILLISECONDS.toHours(totalTiming);
			default:
				return (int) TimeUnit.MILLISECONDS.toMinutes(totalTiming);
		}
	}
}
