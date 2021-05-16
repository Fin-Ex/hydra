package sf.l2j.gameserver.scripting.tasks;

import sf.l2j.gameserver.Shutdown;
import sf.l2j.gameserver.scripting.ScheduledQuest;

public final class ServerRestart extends ScheduledQuest {

	private static final int PERIOD = 600; // 10 minutes

	public ServerRestart() {
		super(-1, "tasks");
	}

	@Override
	public final void onStart() {
		new Shutdown(PERIOD, true).start();
	}

	@Override
	public final void onEnd() {
	}
}
