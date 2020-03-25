/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import java.util.HashSet;
import java.util.Set;
import net.sf.finex.enums.ESocialAction;
import static net.sf.finex.model.GLT.StageInstructing.INSTRUCTOR_LOCS;
import net.sf.finex.model.movie.ActionData;
import net.sf.finex.model.movie.MessageData;
import net.sf.finex.model.movie.MovieManager;
import net.sf.finex.model.movie.actions.ActDeleteNPC;
import net.sf.finex.model.movie.actions.ActMount;
import net.sf.finex.model.movie.actions.ActMove;
import net.sf.finex.model.movie.actions.ActRotate;
import net.sf.finex.model.movie.actions.ActRunnableAction;
import net.sf.finex.model.movie.actions.ActSocial;
import net.sf.finex.model.movie.actions.ActSpawn;
import net.sf.finex.model.movie.actions.ActSpeak;
import net.sf.finex.model.movie.actions.ActTeleport;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.GLTNpc;
import net.sf.l2j.gameserver.model.item.instance.type.HunterCardInstance;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.instance.type.TicketInstance;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.scripting.QuestState;

/**
 *
 * @author finfan
 */
public class StageFinish implements IStageHandler {

	private Set<GLTParticipant> winners;

	private static final Location HUNTER_SPAWN = new Location(0, 0, 0);
	
	@Override
	public void call() {
		World.getInstance().broadcastSystemMessagePacket(SystemMessageId.THE_GRAND_LETHAL_TOURNAMENT_IS_OVER);

		// set all winners and start all movie actions
		findWinners();
		startMovie();

		// clear all
		clear();
		GLTController.getInstance().clear();
	}

	private void findWinners() {
		winners = new HashSet<>();
		for (GLTParticipant participant : GLTController.getInstance().getParticipants()) {
			final Player pc = participant.getPlayer();
			int points = 0;
			for (ItemInstance next : pc.getInventory().getItems()) {
				if (next instanceof TicketInstance) {
					final TicketInstance ticket = (TicketInstance) next;
					if (ticket.getNumber() == participant.getOwnNumber()) {
						points += 3;
					} else if (ticket.getNumber() == participant.getHuntNumber()) {
						points += 3;
					} else {
						points++;
					}
				}
			}

			if (points >= 6) {
				winners.add(participant);
				log.info("Add winner: {} with {} points!", pc, points);
			}
		}
		log.info("All winners founded. Count: {}", winners.size());
	}

	private void startMovie() {
		if (winners.isEmpty()) {
			return;
		}

		// set winners to wyverns and send them to instructor
		for (GLTParticipant participant : winners) {
			final MovieManager movie = new MovieManager();
			{
				final Player player = participant.getPlayer();
				movie.addAction(new ActionData(new ActMount(12621, player))); // sit on wyverns
				movie.addAction(new ActionData(new ActMove(StageInstructing.WYVERN_FLY_TO, player)));
				movie.addAction(new ActionData(new ActTeleport(INSTRUCTOR_LOCS[2], player).setOffset(120)));
				movie.addAction(new ActionData(new ActMount(0, player)));
			}
			movie.startMovie();
		}

		final MovieManager mainMovie = new MovieManager();
		{
			winners.forEach(participant -> mainMovie.addActor(participant.getPlayer()));
			ActSpawn<GLTNpc> actSpawn = new ActSpawn(50002, INSTRUCTOR_LOCS[1], INSTRUCTOR_LOCS[0]).setInvisible();
			mainMovie.addAction(new ActionData(actSpawn));
			final GLTNpc instructor = actSpawn.getNpc();

			// players turn to instrcutor
			winners.forEach(participant -> mainMovie.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, participant.getPlayer(), instructor))));

			mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("My congratulations."))));
			
			final int participantSize = GLTController.getInstance().size();
			final int winnerSize = winners.size();
			
			final boolean $10 = winnerSize <= (int) Math.ceil(participantSize * 0.1);
			final boolean $25 = winnerSize <= (int) Math.ceil(participantSize * 0.25);
			final boolean $50 = winnerSize <= (int) Math.ceil(participantSize * 0.5);
			
			if ($10) {
				// 10% (or less) of winners from all
				mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("As expected, there are only a few of you left... Congratulations to all of you."))));
			} else if ($25) {
				// 25% (or less) of winners from all
				mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Well ... this result is quite acceptable. I could not have imagined that there would be so many of you. Congratulations to all of you."))));
			} else if ($50) {
				// 50% (or less) of winners from all
				mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("Today, surprisingly, I came across the strongest students. I have never seen anyone take more than 25%! Well done! Congratulations to all of you!"))));
			} else {
				// more than 50%
				mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("WHAT?! How can this be? How could over 50% of the participants take the exam ?! Need to complicate it immediately, I will talk with advice in the guild."))));
			}
			
			mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("In any case, you all rightfully become hunters."))));
			mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData("From this day, you are members of our guild of hunters."))));

			actSpawn = new ActSpawn(50009, HUNTER_SPAWN).setInvisible();
			mainMovie.addAction(new ActionData(actSpawn));
			final GLTNpc hunter = actSpawn.getNpc();

			mainMovie.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, instructor, hunter)));
			mainMovie.addAction(new ActionData(new ActSpeak(instructor, new MessageData(hunter.getName() + " drag hunter identification cards here for our winners."))));
			mainMovie.addAction(new ActionData(new ActSpeak(hunter, new MessageData("Your word is my hunt sir!")).setSpeechType(ActSpeak.ESpeechType.NORMAL)));
			mainMovie.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_DEFAULT, instructor)));

			winners.forEach(participant -> {
				final Player player = participant.getPlayer();
				mainMovie.addAction(new ActionData(new ActMove(player.getPosition(), hunter).setNear()));
				mainMovie.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, hunter)));
				mainMovie.addAction(new ActionData(new ActSpeak(hunter, new MessageData("Here is yours Hunter ID Card, take my congradulations.")).setSpeechType(ActSpeak.ESpeechType.NORMAL)));
				mainMovie.addAction(new ActionData(new ActRunnableAction(() -> destroyGltItemsAndReward(player, instructor))));
				mainMovie.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, player, instructor)));
				mainMovie.addAction(new ActionData(new ActSocial(ESocialAction.Bow, player)));
			});

			mainMovie.addAction(new ActionData(new ActMove(HUNTER_SPAWN, hunter)));
			mainMovie.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_DEFAULT, hunter)));
			
			// Delete all npcs, finalize event
			mainMovie.addAction(new ActionData(new ActDeleteNPC(mainMovie, hunter)));
			mainMovie.addAction(new ActionData(new ActDeleteNPC(mainMovie, instructor)));
		}
		mainMovie.startMovie();
	}

	@Override
	public void clear() {
		winners.clear();
		winners = null;
	}

	private void destroyGltItemsAndReward(Player player, GLTNpc instructor) {
		player.getInventory().destroyAllItemsByType(EtcItemType.GLT_ITEM, "GLT_Finish_items_destroy", instructor);
		if (player.getInventory().getHunterCardInstance() != null) {
			// update old card by increasing their timestamp value
			player.getInventory().getHunterCardInstance().update();
			log.info("timestamp of hunter card for {} - was updated", player);
		} else {
			// create new card
			final HunterCardInstance hunterCardID = ItemTable.getInstance().createItem(HunterCardInstance.class, "Hunter_Card", 9216, 1, player, null);
			player.addItem("GLT_Reward", hunterCardID, null, true);
			player.sendPacket(new PlaySound(QuestState.SOUND_ITEMGET));
			hunterCardID.insert();
			log.info("hunter card added to {}", player);
		}
	}
}
