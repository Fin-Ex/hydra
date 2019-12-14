package net.sf.l2j.gameserver.scripting.tasks;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.scripting.ScheduledQuest;

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
