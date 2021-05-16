package sf.l2j.gameserver.scripting.tasks;

import sf.l2j.commons.concurrent.ThreadPool;

import sf.l2j.gameserver.instancemanager.SevenSigns;
import sf.l2j.gameserver.instancemanager.SevenSignsFestival;
import sf.l2j.gameserver.scripting.Quest;

public final class SevenSignsUpdate extends Quest implements Runnable {

	public SevenSignsUpdate() {
		super(-1, "tasks");

		ThreadPool.scheduleAtFixedRate(this, 3600000, 3600000);
	}

	@Override
	public final void run() {
		if (!SevenSigns.getInstance().isSealValidationPeriod()) {
			SevenSignsFestival.getInstance().saveFestivalData(false);
		}

		SevenSigns.getInstance().saveSevenSignsData();
		SevenSigns.getInstance().saveSevenSignsStatus();

		_log.info("SevenSigns: Data has been successfully saved.");
	}
}
