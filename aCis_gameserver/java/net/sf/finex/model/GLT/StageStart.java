/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import it.sauronsoftware.cron4j.Scheduler;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import net.sf.finex.events.EventBus;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnKill;
import net.sf.l2j.gameserver.model.actor.events.OnLogout;
import net.sf.l2j.gameserver.model.actor.events.OnZoneSet;
import net.sf.l2j.gameserver.model.actor.instance.GLTNpc;
import net.sf.l2j.gameserver.model.actor.instance.GLTTrader;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.zone.form.ZoneCylinder;
import net.sf.l2j.gameserver.model.zone.type.L2GLTBushZone;
import net.sf.l2j.gameserver.model.zone.type.L2GLTZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author finfan
 */
public class StageStart implements IStageHandler {

	@Getter private EventBus listener;
	private GLTTrader Auriel, Marko;
	private SpawnLocation[] traderSpawnPoints;
	private Scheduler traderTimer;
	
	private List<GLTNpc> bushes;
	private GLTSupplyTimer supplyTimer;
	private GLTTimingTask timingTask;
	
	@Override
	public void call() {
		listener = new EventBus();
		GLTController.getInstance().getZone().activate();
		timingTask = new GLTTimingTask();
		initializeListeners();
		initializeTimers();
		initializeBushes();
		initializeTraders();
		World.getInstance().broadcastSystemMessagePacket(SystemMessageId.THE_GRAND_LETHAL_TOURNAMENT_BEGINS);
		GLTController.getInstance().startNextTimer(LocalTime.now().plusMinutes(60).getMinute() + " " + LocalTime.now().plusHours(1).getHour() + " * * *");
	}

	// Stage Initializers ///////////////////////////////////////////////////////////////// 
	
	private void initializeListeners() {
		listener.subscribe().cast(OnLogout.class).forEach(event -> {
			//TODO
			//tickets drop to the ground?
			//notify all participants about it and show the tickets on map
		});
		
		listener.subscribe().cast(OnZoneSet.class).forEach(event -> {
			if (event.getZoneId() != ZoneId.GLT || !event.getCreature().isPlayer()) {
				return;
			}

			final GLTParticipant participant = GLTController.getInstance().getParticipant(event.getCreature().getPlayer());
			if (participant == null) {
				return;
			}

			final L2GLTZone zone = GLTController.getInstance().getZone();
			final Creature cha = event.getCreature();
			if (!event.isEntering() && !zone.getForm().isInsideZone(cha.getX(), cha.getY(), cha.getZ())) {
				timingTask.add(participant);
			} else {
				timingTask.remove(participant);
			}
		});
		
		listener.subscribe().cast(OnKill.class).forEach(event -> {
			final Creature killer = event.getKiller();
			final Creature victim = event.getVictim();

			if (!victim.isPlayer()) {
				return;
			}

			if (!GLTController.getInstance().isParticipate(victim.getPlayer())) {
				return;
			}

			final Player victimPlayer = victim.getPlayer();
			for (ItemInstance next : victimPlayer.getInventory().getItems()) {
				if (next == null) {
					continue;
				}

				if (next.getItemType() == EtcItemType.GLT_ITEM) {
					victimPlayer.dropItem("GLT_onKill", next, victimPlayer, true);
				}
			}

			GLTController.getInstance().broadcast(SystemMessage.getSystemMessage(SystemMessageId.S1_KILLED_S2).addCharName(killer).addCharName(victim));
		});
		log.info("Listeners was initialized.");
	}
	
	private void initializeTimers() {
		supplyTimer = new GLTSupplyTimer(Rnd.get(7, 10));
		supplyTimer.invoke();
		log.info("Supply timer was scheduled.");
	}
	
	private void initializeBushes() {
		bushes = new ArrayList<>();
		ZoneManager.getInstance().getAllZones(L2GLTBushZone.class)
				.stream()
				.map((bushZone) -> ((ZoneCylinder) bushZone.getForm()))
				.forEachOrdered((cyl) -> {
					bushes.add((GLTNpc) L2Spawn.create(50005, cyl.getX(), cyl.getY(), cyl.getLowZ() + 100, Rnd.get(Short.MAX_VALUE)));
				});
		log.info("Bushes are all spawned.");
	}
	
	private void initializeTraders() {
		// initialize all trader spawn elements
		traderSpawnPoints = new SpawnLocation[] {
			new SpawnLocation(10584, -22328, -3656, 55974),
			new SpawnLocation(11512, -22968, -3616, 39099),
			new SpawnLocation(11992, -24664, -3640, 23310),
			new SpawnLocation(7544, -22632, -3656, 5530),
			new SpawnLocation(6168, -22504, -3104, 16720),
			new SpawnLocation(9048, -13976, -3752, 24575),
			new SpawnLocation(9080, -13624, -3768, 34932),
			new SpawnLocation(7368, -5416, -3208, 55013),
			new SpawnLocation(7272, -5464, -3232, 55370),
			new SpawnLocation(6472, -3080, -2960, 19497),
			new SpawnLocation(6536, -2952, -2960, 39449),
			new SpawnLocation(6296, -2232, -2960, 37355),
			new SpawnLocation(6312, -1992, -2960, 37069),
			new SpawnLocation(22200, -18984, -2880, 39595),
			new SpawnLocation(29048, -15208, -2344, 36964),
			new SpawnLocation(25656, -14008, -2504, 39373),
			new SpawnLocation(28616, -12264, -2176, 35594),
			new SpawnLocation(21384, -7080, -2128, 57892),
			new SpawnLocation(22200, -5976, -1968, 46352),
			new SpawnLocation(23720, -8488, -1344, 42482),
			new SpawnLocation(18024, -6968, -2792, 42035),
			new SpawnLocation(15224, -6504, -3040, 55491),
			new SpawnLocation(12984, -9288, -3096, 62035),
			new SpawnLocation(12744, -9400, -3080, 53868),
			new SpawnLocation(12904, -8648, -3024, 61288),
			new SpawnLocation(12984, -12536, -2968, 61951),
			new SpawnLocation(13016, -12424, -2952, 58589),
			new SpawnLocation(12792, -14456, -3184, 48038),
			new SpawnLocation(12680, -14568, -3160, 9223),
			new SpawnLocation(12872, -13688, -3216, 64644),
			new SpawnLocation(12872, -13752, -3232, 1221)
		};
		
		// shuffle all elements
		Collections.shuffle(Arrays.asList(traderSpawnPoints));
		traderTimer = new Scheduler();
		// each 10 minutes teleport to another location
		traderTimer.schedule("*/10 * * * *", () -> {
			// spawn Auriel with all support potions and etc...
			final SpawnLocation spawnLoc = Rnd.get(traderSpawnPoints);
			if (Auriel == null) {
				Auriel = (GLTTrader) L2Spawn.create(50007, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), spawnLoc.getHeading());
			} else {
				Auriel.teleToLocation(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), 0);
			}
			log.info("Auriels coords: {},{},{}", Auriel.getX(), Auriel.getY(), Auriel.getZ());

			// spawn Marko with throwable weapon
			final SpawnLocation spawnLoc2 = Rnd.get(traderSpawnPoints);
			if (Marko == null) {
				Marko = (GLTTrader) L2Spawn.create(50008, spawnLoc2.getX(), spawnLoc2.getY(), spawnLoc2.getZ(), spawnLoc2.getHeading());
			} else {
				Marko.teleToLocation(spawnLoc2.getX(), spawnLoc2.getY(), spawnLoc2.getZ(), 0);
			}
			log.info("Marko coords: {},{},{}", Marko.getX(), Marko.getY(), Marko.getZ());
			
			// send message to all participants about changing position of traders
			GLTController.getInstance().broadcast(SystemMessageId.GLT_TRADERS_CHANGE_LOCATION);
		});
		traderTimer.start();
		log.info("Traders are initialized!");
	}

	@Override
	public void clear() {
		synchronized (this) {
			listener.unsubscribeAll();
			bushes.forEach(b -> b.deleteMe());
			bushes.clear();
			bushes = null;
			supplyTimer.stop();
			supplyTimer = null;
			timingTask.clear();
			timingTask = null;
			if (Auriel != null) {
				Auriel.deleteMe();
				Auriel = null;
			}
			if (Marko != null) {
				Marko.deleteMe();
				Marko = null;
			}
			traderSpawnPoints = null;
			traderTimer.stop();
			traderTimer = null;
		}
	}
	
	///////////////////////////////////////////////////////////////// Event realization
	
	@Data
	private static class TradeSpawnPoint {
		private boolean busy;
		private final SpawnLocation loc;
	}
}
