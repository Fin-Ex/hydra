package sf.l2j.gameserver.scripting.tasks;

import sf.l2j.gameserver.data.sql.ClanTable;
import sf.l2j.gameserver.scripting.ScheduledQuest;

public final class ClansLadder extends ScheduledQuest {

	public ClansLadder() {
		super(-1, "tasks");
	}

	@Override
	public final void onStart() {
		ClanTable.getInstance().refreshClansLadder(true);
	}

	@Override
	public final void onEnd() {
	}
}
