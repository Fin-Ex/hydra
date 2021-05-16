package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import java.util.concurrent.ScheduledFuture;

import sf.l2j.commons.concurrent.ThreadPool;
import sf.l2j.commons.random.Rnd;

import sf.l2j.gameserver.geoengine.GeoEngine;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.MoveToPawn;

public class TownPet extends Folk {

	private ScheduledFuture<?> _aiTask;

	public TownPet(int objectId, NpcTemplate template) {
		super(objectId, template);
		setRunning();

		_aiTask = ThreadPool.scheduleAtFixedRate(new RandomWalkTask(), 1000, 10000);
	}

	@Override
	public void onAction(Player player) {
		// Set the target of the player
		if (player.getTarget() != this) {
			player.setTarget(this);
		} else {
			if (!canInteract(player)) {
				player.getAI().setIntention(CtrlIntention.INTERACT, this);
			} else {
				// Rotate the player to face the instance
				player.sendPacket(new MoveToPawn(player, this, INTERACTION_DISTANCE));

				// Send ActionFailed to the player in order to avoid he stucks
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
	}

	@Override
	public void deleteMe() {
		if (_aiTask != null) {
			_aiTask.cancel(true);
			_aiTask = null;
		}
		super.deleteMe();
	}

	public class RandomWalkTask implements Runnable {

		@Override
		public void run() {
			if (getSpawn() == null) {
				return;
			}

			getAI().setIntention(CtrlIntention.MOVE_TO, GeoEngine.getInstance().canMoveToTargetLoc(getX(), getY(), getZ(), getSpawn().getLocX() + Rnd.get(-75, 75), getSpawn().getLocY() + Rnd.get(-75, 75), getZ()));
		}
	}
}
