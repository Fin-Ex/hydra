/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import net.sf.finex.enums.ESocialAction;
import net.sf.finex.model.movie.ActionData;
import net.sf.finex.model.movie.EItemManipulation;
import net.sf.finex.model.movie.MessageData;
import net.sf.finex.model.movie.MovieManager;
import net.sf.finex.model.movie.actions.ActCast;
import net.sf.finex.model.movie.actions.ActDeleteNPC;
import net.sf.finex.model.movie.actions.ActItem;
import net.sf.finex.model.movie.actions.ActMount;
import net.sf.finex.model.movie.actions.ActMove;
import net.sf.finex.model.movie.actions.ActRotate;
import net.sf.finex.model.movie.actions.ActRunnableAction;
import net.sf.finex.model.movie.actions.ActSocial;
import net.sf.finex.model.movie.actions.ActSpawn;
import net.sf.finex.model.movie.actions.ActSpeak;
import static net.sf.finex.model.movie.actions.ActSpeak.ESpeechType.NORMAL;
import net.sf.finex.model.movie.actions.ActTeleport;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Folk;
import net.sf.l2j.gameserver.model.actor.instance.GLTNpc;
import net.sf.l2j.gameserver.model.item.instance.type.TicketInstance;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.zone.form.ZoneCylinder;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 *
 * @author finfan
 */
public class StageInstructing implements IStageHandler {

	@Getter private Queue<TicketInstance> tickets;
	
	public static final Location[] INSTRUCTOR_LOCS = {
		new Location(34079, -48215, 1780),	// where spawn instructor
		new Location(33218, -48199, 1780),	// where instructor should go after spawn
		new Location(33268, -48199, 1780)	// where pandora box will spawned
	};
	
	private static final String[] WHERE_AM_I = {
		"Where am I?",
		"What the fuck?!",
		"...",
		"Jesus christ... my head is crushing...",
		"Where is that bastard? I'll knock him out!!!"
	};
	
	private static final String[] TURN = {
		" your turn.",
		" you are next.",
		" come here.",
		" don't stand by the pillar, come.",
		" don't be afraid."
	};
	
	private static final String[] IM_ON_MY_WAY = {
		"I'm on my way.",
		"...",
		"Already.",
		"Here."
	};
	
	private static final String[] GET_HUNTER_NUMBER = {
		"Let's see what is there for me ...",
		"Oops! And here is my number!",
		"Let's throw whose head I have to cut",
		"Whose executioner will I be?",
		"...",
		"Ahahahhahahahah!"
	};
	
	private static final String[] BANDIT_WORDS = {
		"Well well, what we have here... oh another hunter?",
		"You are %targetName% i suppose?",
		"We have little time! We have to go!"
	};
	
	public static final Location WYVERN_FLY_TO = new Location(32540, -48224, 2242);
	
	@Override
	public void call() {
		if (!GLTArbitrator.createTickets()) {
			World.getInstance().broadcastSystemMessagePacket(SystemMessageId.THE_GRAND_LETHAL_TOURNAMENT_IS_RESETED);
			GLTController.getInstance().restart();
			return;
		}
		
		tickets = new ArrayDeque<>();
		tickets.addAll(GLTController.getInstance().getTickets());
		log.info("queue of tickets was created!");
		
		World.getInstance().broadcastSystemMessagePacket(SystemMessageId.THE_GRAND_LETHAL_TOURNAMENT_REGISTRATION_IS_CLOSED);
		
		//////////////////////////////////////////////////////////////////////////////////// Bandit attack
		
		final List<GLTParticipant> hunters = GLTController.getInstance().getParticipants();
		final ZoneCylinder cylinder = (ZoneCylinder) GLTController.getInstance().getBriefingZone().getForm();
		int timing = 0;
		for (GLTParticipant participant : hunters) {
			final Player player = participant.getPlayer();
			if(player == null || !player.isOnline()) {
				GLTController.getInstance().getParticipants().remove(participant);
				continue;
			}
			
			final MovieManager movieManager = new MovieManager();
			{
				final ActSpawn<GLTNpc> actSpawn = new ActSpawn(50003, new Location(player.getX() + 250, player.getY() + 250, player.getZ()), player.getPosition()).setInvisible();
				movieManager.addAction(new ActionData(actSpawn));
				movieManager.addAction(new ActionData(new ActRunnableAction(() -> {
					player.stopAllEffects();
					player.abortAttack();
					player.abortCast();
					player.stopMove(null);
				})));
				movieManager.addAction(new ActionData(new ActMove(new Location(player.getPosition().getX() + 20, player.getPosition().getY() + 20, player.getPosition().getZ()), actSpawn.getNpc(), player).setWalk().setMessage(new MessageData(Rnd.get(BANDIT_WORDS)))));
				movieManager.addAction(new ActionData(new ActCast(4072, 1, actSpawn.getNpc(), player).setSimulate().setMessage(new MessageData("Sorry, but it is necessary!"))));
				movieManager.addAction(new ActionData(new ActSocial(ESocialAction.FakeDeath, player).setMessage(new MessageData("Urghh..."))));
				movieManager.addAction(new ActionData(new ActItem(EItemManipulation.GIVE, actSpawn.getNpc(), player)));
				movieManager.addAction(new ActionData(new ActRunnableAction(() -> {
					final TicketInstance ticket = tickets.poll();
					ticket.setBaseOwner(participant.getPlayer());
					participant.setOwnNumber(ticket.getNumber());
					participant.addTicket(ticket, null);
				})));

				movieManager.addAction(new ActionData(new ActDeleteNPC(movieManager, actSpawn.getNpc())));
				final ActTeleport actTeleport = new ActTeleport(new Location(cylinder.getX(), cylinder.getY(), 1780), player).setOffset(cylinder.getRadius());
				participant.setTeleportLocation(actTeleport.getTeleportLocation());
				movieManager.addAction(new ActionData(actTeleport));
				movieManager.addAction(new ActionData(new ActSocial(ESocialAction.StandUp, player).setMessage(new MessageData(Rnd.get(WHERE_AM_I)))));
				movieManager.addAction(new ActionData(new ActSocial(ESocialAction.Unaware, player)));
			}
			timing += movieManager.getTotalTimingIn(TimeUnit.SECONDS);
			if(player.isDead()) {
				player.doRevive(100);
			}
			movieManager.startMovie();
		}
		
	
		//////////////////////////////////////////////////////////////////////////////////// Instructing
		
		// spawn act
		// get instructor
		// TODO: npc cant initialize cause method call not called at this time
		final MovieManager movieManager = new MovieManager();
		{
			final ActSpawn<GLTNpc> actSpawn = new ActSpawn(50002, INSTRUCTOR_LOCS[0], INSTRUCTOR_LOCS[1]).setInvisible();
			movieManager.addAction(new ActionData(actSpawn));
			final GLTNpc instructor = actSpawn.getNpc();
			final ActSpawn<GLTNpc> pandoraBox = new ActSpawn(50004, INSTRUCTOR_LOCS[2]).setInvisible();
			movieManager.addAction(new ActionData(pandoraBox));
			final Folk box = pandoraBox.getNpc();
			
			// move act
			movieManager.addAction(new ActionData(new ActMove(INSTRUCTOR_LOCS[1], instructor).setWalk().setMessage(new MessageData("I greet you - future hunters."))));
			movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_DEFAULT, instructor).setTiming(1500)));

			// start rotate all player to face to instructor
			for (GLTParticipant participant : hunters) {
				final Player player = participant.getPlayer();
				if (player == null || !player.isOnline()) {
					GLTController.getInstance().getParticipants().remove(participant);
					continue;
				}

				movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, player, instructor)));
			}
			
			// speak act
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("My name is %selfName% and I'm is your instructor, which will tell you about some of the nuances.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("But before we begin, we need to make sure that all the examiners are here.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Each of you received a Hunter Ticket, which has its own number. All tickets are unique, which means that each of your numbers is also unique.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("I want each of you to settle in the order of the queue - giving me your number indicated on the ticket.")).setSpeechType(NORMAL)));

			// start players count
			for (GLTParticipant participant : hunters) {
				final Player player = participant.getPlayer();
				if (player == null || !player.isOnline()) {
					GLTController.getInstance().getParticipants().remove(participant);
					continue;
				}

				movieManager.addAction(new ActionData(new ActSocial(ESocialAction.Waiting, player).setMessage(new MessageData(String.valueOf(participant.getOwnNumber())))));
			}
			
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Well, everything seems to be here. Then let's start the briefing.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("The exam takes place within a certain time interval seted by the president of the Hunter's Guild.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("When the time comes to an end, the exam will be considered completed. For those who passed, wyverns will fly and bring them to me. Here we will summarize the results.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("And those who did not pass ... I do not give a shit about them.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Now, I call you one by one to the Pandora's box, so that you put your hand into it and pull out the target for hunting...")).setSpeechType(NORMAL)));
			
			final Location nearBox = new Location(INSTRUCTOR_LOCS[2].getX() + 20, INSTRUCTOR_LOCS[2].getY(), INSTRUCTOR_LOCS[2].getZ());
			for (GLTParticipant participant : hunters) {
				final Player player = participant.getPlayer();
				if (player == null || !player.isOnline()) {
					GLTController.getInstance().getParticipants().remove(participant);
					continue;
				}
			
				movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData(participant.getName() + Rnd.get(TURN)))));
				movieManager.addAction(new ActionData(new ActMove(nearBox, player).setMessage(new MessageData(Rnd.get(IM_ON_MY_WAY)))));
				movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, player, box).setTiming(1500)));
				movieManager.addAction(new ActionData(new ActSocial(ESocialAction.Pickup, player).setMessage(new MessageData(Rnd.get(GET_HUNTER_NUMBER)))));
				movieManager.addAction(new ActionData(new ActRunnableAction(() -> GLTController.getInstance().giveTargetNumber(participant))));
				movieManager.addAction(new ActionData(new ActMove(participant.getTeleportLocation(), player)));
				movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, player, instructor).setTiming(1500)));
			}

			movieManager.addAction(new ActionData(new ActDeleteNPC(movieManager, box)));
			
			// tell rules and move to spawn points
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Good.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("A bit of information about the recruitment and loss of hunter points.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("...The Hunter ticket that you received when you came here has a strength of 3 points.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Each additional ticket in your inventory from a hostile participant gives an additional 1 point.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("If you managed to get a ticket for your target, then it will bring you an additional 3 points.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Those who pass the exam are considered to be those who, upon completion of the exam, will have a total of at least 6 points.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("We wave the rest of the pen until the next retake.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Also I must remind all of you...")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("#1 Do not leave the participation area! You can leave it only after the exam is completed.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("#2 Do not forget about the drop-down things and Herbs! Perhaps they will save your life!")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("#3 Use traps. A true hunter knows when and where to set up a trap. The right hunting strategy is part of your future victory.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("#4 Use the terrain to the maximum, wait lurking - if necessary, attack when the victim does not expect this.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("That's all I wanted to say.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActMove(INSTRUCTOR_LOCS[0], instructor).setWalk()));
			movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.HALF, instructor).setTiming(1500)));

			for (GLTParticipant participant : hunters) {
				final Player player = participant.getPlayer();
				if (player == null || !player.isOnline()) {
					GLTController.getInstance().getParticipants().remove(participant);
					continue;
				}
			
				movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, player, instructor)));
			}

			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Almost forgot, from moment Grand Lethal Tournament is considered open!")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActSpeak(instructor, new MessageData("These little \"birds\" will take you to the exam zone. Good luck to you all.")).setSpeechType(NORMAL)));
			movieManager.addAction(new ActionData(new ActDeleteNPC(movieManager, instructor)));

			// teleport to the island all of participants
			for (GLTParticipant participant : hunters) {
				final Player player = participant.getPlayer();
				if (player == null || !player.isOnline()) {
					GLTController.getInstance().getParticipants().remove(participant);
					continue;
				}

				movieManager.addAction(new ActionData(new ActMount(12621, player))); //wyvern ride
				movieManager.addAction(new ActionData(new ActMove(WYVERN_FLY_TO, player)));
				movieManager.addAction(new ActionData(new ActTeleport(GLTController.getInstance().getZone().getSpawnLoc(), player)));
				movieManager.addAction(new ActionData(new ActMount(0, player)));
			}
		}
		timing += movieManager.getTotalTimingIn(TimeUnit.SECONDS);
		GLTController.getInstance().startNextTimer(LocalTime.now().plusSeconds(timing).getMinute() + " " + LocalTime.now().getHour() + " * * *");
		movieManager.startMovie();
	}

	@Override
	public void clear() {
		if(tickets != null) {
			tickets.clear();
		}
		tickets = null;
	}
}
