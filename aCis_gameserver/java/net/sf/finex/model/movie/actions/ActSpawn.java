/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import lombok.Getter;
import net.sf.finex.enums.EMovieAction;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.location.Location;

/**
 *
 * @author finfan
 * @param <T>
 */
public class ActSpawn<T extends Npc> extends AbstractActorAction {
	
	private boolean respawn;
	private int despawnTime;
	private boolean running;
	private boolean invisibleXYZ;
	
	@Getter private T npc;

	public ActSpawn(int npcId, Location spawnLoc, Location headingLoc) {
		try {
			final NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
			final L2Spawn spawn = new L2Spawn(template);
			final int heading = headingLoc != null ? MathUtil.calculateHeadingFrom(spawnLoc.getX(), spawnLoc.getY(), headingLoc.getX(), headingLoc.getY()) : 0;
			spawn.setLoc(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), heading);
			spawn.setRespawnState(respawn);
			npc = (T) spawn.doSpawn(false, invisibleXYZ);
			npc.setFullHpMpCp();

			if (running) {
				npc.setRunning();
			}

			if (despawnTime > 0) {
				npc.scheduleDespawn(despawnTime);
			}

			if (message != null) {
				npc.broadcastNpcSay(message.getMessage());
			}

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ActSpawn(int npcId, Location spawnLoc) {
		this(npcId, spawnLoc, null);
	}

	@Override
	public final void call() {
	}

	public ActSpawn setInvisible() {
		this.invisibleXYZ = true;
		return this;
	}

	public ActSpawn setRespawn() {
		this.respawn = true;
		return this;
	}

	public ActSpawn setDespawnTime(int despawnTime) {
		this.despawnTime = despawnTime;
		return this;
	}

	public ActSpawn setRunning() {
		this.running = true;
		return this;
	}
	
	@Override
	public EMovieAction getType() {
		return EMovieAction.SPAWN;
	}

	@Override
	public long getTiming() {
		return 0;
	}
	
}
