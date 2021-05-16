package sf.l2j.gameserver.scripting.tasks;

import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.UserInfo;
import sf.l2j.gameserver.scripting.ScheduledQuest;

public final class Recommendation extends ScheduledQuest {

	public Recommendation() {
		super(-1, "tasks");
	}

	@Override
	public final void onStart() {
		for (Player player : World.getInstance().getPlayers()) {
			player.restartRecom();
			player.sendPacket(new UserInfo(player));
		}

		_log.info("Recommendation: Recommendation has been reset.");
	}

	@Override
	public final void onEnd() {
	}
}
