/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.finex.model.movie.ActionData;
import net.sf.finex.model.movie.MessageData;
import net.sf.finex.model.movie.MovieManager;
import net.sf.finex.model.movie.actions.ActAttack;
import net.sf.finex.model.movie.actions.ActDeleteNPC;
import net.sf.finex.model.movie.actions.ActMove;
import net.sf.finex.model.movie.actions.ActRotate;
import net.sf.finex.model.movie.actions.ActSpawn;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.GLTNpc;
import net.sf.l2j.gameserver.model.location.Location;

/**
 *
 * @author finfan
 */
public class GLTTimingTask implements Runnable {

	private final Future<?> task;
	private final List<GLTParticipant> outsiders;

	public GLTTimingTask() {
		this.task = ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
		this.outsiders = new CopyOnWriteArrayList<>();
	}

	public void add(GLTParticipant participant) {
		if (outsiders.contains(participant)) {
			return;
		}

		outsiders.add(participant);
		participant.getPlayer().sendMessage("You leave the GLT zone while you are participate! Your safety time will be decreased for 1 point every 1 seconds while you are not inside in GLT zone.");
	}

	public void remove(GLTParticipant participant) {
		if (!outsiders.contains(participant)) {
			return;
		}

		outsiders.remove(participant);
		participant.getPlayer().sendMessage("You returned to zone. Your current safety time is: " + participant.safetyTime);
	}

	private static final String[] BANDIT_TEXT = {
		"What do we have here? Another coward?",
		"*mumbles to himself* Do it, do it. I'm so tired of you...",
		"You should not have done it, friend..."
	};

	@Override
	public void run() {
		if (outsiders.isEmpty()) {
			return;
		}

		for (GLTParticipant participant : outsiders) {
			if (participant.safetyTime <= 0) {
				final Player pc = participant.getPlayer();
				if (pc == null || !pc.isOnline()) {
					remove(participant);
					continue;
				}

				final MovieManager movieManager = new MovieManager();
				{
					final ActSpawn<GLTNpc> actSpawn = (ActSpawn<GLTNpc>) new ActSpawn(50003, new Location(pc.getX() + 200, pc.getY() + 200, pc.getZ())).setMessage(new MessageData(Rnd.get(BANDIT_TEXT)));
					final GLTNpc bandit = actSpawn.getNpc();
					bandit.setWalking();
					movieManager.addAction(new ActionData(actSpawn));
					movieManager.addAction(new ActionData(new ActMove(new Location(pc.getX() + 20, pc.getY() + 20, pc.getZ()), bandit, pc)));
					movieManager.addAction(new ActionData(new ActRotate(ActRotate.ERotateType.TO_TARGET, pc, bandit).setMessage(new MessageData("Fuck..."))));
					final DamageInfo info = new DamageInfo();
					info.damage = 99999;
					info.isMiss = false;
					info.isParry = false;
					info.shieldResult = 0;
					info.isCrit = true;
					movieManager.addAction(new ActionData(new ActAttack(bandit, pc).setInfo(info).setMessage(new MessageData("YOU ARE DEAD!"))));
					movieManager.addAction(new ActionData(new ActDeleteNPC(movieManager, bandit)));
				}
				movieManager.startMovie();
				remove(participant);
				return;
			}

			participant.safetyTime--;
		}
	}

	public void clear() {
		task.cancel(false);
		outsiders.clear();
	}
}
