/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.zone.type;

import java.util.List;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.model.GLT.GLTController;
import net.sf.finex.model.GLT.GLTParticipant;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.WorldRegion;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.Revive;

/**
 *
 * @author finfan
 */
@Slf4j
public class L2GLTZone extends L2SpawnZone implements Runnable {

	private static Future<?> watchTask;
	private GLTParticipant[] corpses;

	public L2GLTZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		if (!character.isPlayer()) {
			return;
		}

		if (!GLTController.getInstance().isParticipate(character.getPlayer())) {
			return;
		}

		character.setInsideZone(ZoneId.GLT, true);
	}

	@Override
	protected void onExit(Creature character) {
		if (!character.isInsideZone(ZoneId.GLT)) {
			return;
		}

		character.setInsideZone(ZoneId.GLT, false);
	}

	@Override
	public void run() {
		if (corpses != null) {
			reviveActors();
		} else {
			log.info("is empty corpses list");
		}
	}

	@Override
	public void onReviveInside(Creature character) {
	}

	@Override
	public void onDieInside(Creature character) {
		if (character.isPlayer() && GLTController.getInstance().isParticipate(character.getPlayer())) {
			character.sendMessage("[GLT Service] You will be resurrected soon...");
		}
	}

	public void activate() {
		final List<GLTParticipant> participants = GLTController.getInstance().getParticipants();
		corpses = new GLTParticipant[participants.size()];
		for (int i = 0; i < corpses.length; i++) {
			corpses[i] = participants.get(i);
		}
		watchTask = ThreadPool.scheduleAtFixedRate(this, 60_000, 60_000);
		log.info("Zone: {} activated for {} pariticipants!", this, corpses.length);
	}

	@Override
	public void onDestroy() {
		reviveActors();
		corpses = null;
		watchTask.cancel(false);
		watchTask = null;
		super.onDestroy();
	}

	private void reviveActors() {
		for (GLTParticipant next : corpses) {
			final Player player = next.getPlayer();
			if (player.isDead()) {
				player.teleToLocation(getSpawnLoc(), 500);
				player.setIsDead(false);
				player.broadcastPacket(new Revive(player));
				player.setFullHpMpCp();
				final WorldRegion region = player.getRegion();
				if (region != null) {
					region.onRevive(player);
				}
			}
		}
	}
}
