/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 *
 * @author finfan
 */
@Slf4j
public class Trap extends Attackable implements Runnable {

	private final Creature owner;
	private Future<?> lifecycle;
	@Setter private Runnable implcode;
	
	public Trap(int objectId, NpcTemplate template, Creature owner) {
		super(objectId, template);
		this.owner = owner;
	}

	@Override
	public boolean isAutoAttackable(Creature attacker) {
		return owner.isAutoAttackable(attacker);
	}
	
	public void activate(int firstTime, int period) {
		if(period <= 0) {
			lifecycle = ThreadPool.schedule(this, TimeUnit.SECONDS.toMillis(firstTime));
		} else {
			lifecycle = ThreadPool.scheduleAtFixedRate(this, TimeUnit.SECONDS.toMillis(firstTime), TimeUnit.SECONDS.toMillis(period));
		}
		log.info("trap {} activated!", this);
	}
	
	public void deactivate() {
		if(lifecycle != null) {
			lifecycle.cancel(false);
			lifecycle = null;
		}
		log.info("trap {} deactivated!", this);
	}
	
	@Override
	public boolean isMovementDisabled() {
		return true;
	}

	@Override
	public boolean isAttackingDisabled() {
		return true;
	}

	@Override
	public void run() {
		implcode.run();
	}
	
	public static final Trap spawn(int idTemplate, int x, int y, int z, int despawnSeconds) {
		try {
			final NpcTemplate template = NpcTable.getInstance().getTemplate(idTemplate);
			final L2Spawn spawn = new L2Spawn(template);
			spawn.setLoc(x, y, z, Rnd.get(Short.MAX_VALUE * 2));
			spawn.setRespawnState(false);
			final Trap npc = (Trap) spawn.doSpawn(false, false);
			npc.setFullHpMpCp();
			if(despawnSeconds > 0) {
				npc.scheduleDespawn(despawnSeconds * 1000);
			}
			return npc;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
