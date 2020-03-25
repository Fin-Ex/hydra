/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import it.sauronsoftware.cron4j.Scheduler;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.TicketInstance;
import net.sf.l2j.gameserver.model.zone.type.L2GLTZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 *
 * @author finfan
 */
@Slf4j
public final class GLTController {

	@Getter private static final GLTController instance = new GLTController();
	
	@Getter private List<GLTParticipant> participants;
	@Getter private EStage stage;
	@Getter private Scheduler timer;
	@Getter private List<TicketInstance> tickets;
	@Getter private L2GLTZone zone, briefingZone;
	
	private GLTController() {
	}

	public void restart() {
		if(timer != null && timer.isStarted()) {
			timer.stop();
		}
		clear();
		startNextTimer(LocalTime.now().plusMinutes(2).getMinute() + " " + LocalTime.now().getHour() + " * * *");
		timer.start();
		briefingZone = ZoneManager.getInstance().getZoneById(110000, L2GLTZone.class);
		zone = ZoneManager.getInstance().getZoneById(110001, L2GLTZone.class);
	}
	
	public void launchStage(EStage stage) {
		if(this.stage != null) {
			this.stage.getHandler().clear();
		}
		
		this.stage = stage;
		
		switch(this.stage) {
			case REGISTER:
				participants = new CopyOnWriteArrayList<>();
				register(World.getInstance().getPlayer(268480262));
				register(World.getInstance().getPlayer(268480271));
				register(World.getInstance().getPlayer(268480269));
				register(World.getInstance().getPlayer(268480281));
				//register(World.getInstance().getPlayer(268480262));
				//register(World.getInstance().getPlayer(268480262));
				break;
				
			case INSTRUCTING:
				tickets = new CopyOnWriteArrayList<>();
				break;
		}
		
		this.stage.getHandler().call();
	}
	
	public void register(Player player) {
		if(participants.size() == GLTSettings.MAX_PARTICIPANTS) {
			player.sendMessage("Hunter examination already have a maximum participants.");
			return;
		}
		
		participants.add(new GLTParticipant(player));
		log.info("{} was registered and became participant.", player);
		player.sendMessage("You registration was accepted.");
	}

	public boolean isParticipate(Player player) {
		if(participants == null || participants.isEmpty()) {
			return false;
		}
		
		return participants.stream().anyMatch((participant) -> (participant.getObjectId() == player.getObjectId()));
	}

	public GLTParticipant getParticipant(Player player) {
		for (GLTParticipant participant : participants) {
			if (participant.getObjectId() == player.getObjectId()) {
				return participant;
			}
		}
		
		return null;
	}
	
	public GLTParticipant getParticipant(int objectId) {
		for (GLTParticipant participant : participants) {
			if (participant.getObjectId() == objectId) {
				return participant;
			}
		}
		
		return null;
	}
	
	public void clear() {
		stage = null;
		if (tickets != null) {
			tickets.clear();
			tickets = null;
		}
		if (participants != null) {
			participants.clear();
			participants = null;
		}
		if (zone != null) {
			zone.onDestroy();
			zone = null;
		}
		log.info("Event cleared fully.");
	}
	
	private class Task implements Runnable {
		private final EStage stage;

		public Task(EStage next) {
			this.stage = next;
		}

		@Override
		public void run() {
			launchStage(stage);
		}
	}

	public void startNextTimer(String pattern) {
		if(timer == null) {
			timer = new Scheduler();
		}
		
		if (stage == null) {
			timer.schedule(pattern, new Task(EStage.REGISTER));
			log.info("GLT scheduler was started!");
		} else {
			switch (stage) {
				case REGISTER:
					timer.schedule(pattern, new Task(EStage.INSTRUCTING));
					break;

				case INSTRUCTING:
					EStage.REGISTER.getHandler().clear();
					timer.schedule(pattern, new Task(EStage.START));
					break;

				case START:
					EStage.INSTRUCTING.getHandler().clear();
					timer.schedule(pattern, new Task(EStage.FINISH));
					break;

				case FINISH:
					EStage.START.getHandler().clear();
					timer.stop();
					break;
					
				default:
					timer.stop();
					break;
			}
		}
		log.info("Schedule: {}", pattern);
	}
	
	public void giveTargetNumber(GLTParticipant exceptParticipant) {
		for (TicketInstance ticket : tickets) {
			if(ticket.getBaseOwner() != exceptParticipant.getPlayer() && !ticket.isBusy()) {
				exceptParticipant.setHuntNumber(ticket.getNumber());
				exceptParticipant.getPlayer().sendMessage("You receive the target number: " + ticket.getNumber());
				ticket.setBusy(true);
				log.info("{} received a target number [{}]", exceptParticipant.getPlayer(), exceptParticipant.getHuntNumber());
				break;
			}
		}
	}
	
	public void broadcast(SystemMessageId msg) {
		for(GLTParticipant participant : participants) {
			final Player player = participant.getPlayer();
			if(player == null || !player.isOnline()) {
				continue;
			}
			
			player.sendPacket(msg);
		}
	}
	
	public void broadcast(L2GameServerPacket packet) {
		for(GLTParticipant participant : participants) {
			final Player player = participant.getPlayer();
			if(player == null || !player.isOnline()) {
				continue;
			}
			
			player.sendPacket(packet);
		}
	}
	
	public int size() {
		return participants.size();
	}
}
