package net.sf.l2j.gameserver.scripting.tasks;

import org.slf4j.LoggerFactory;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.scripting.Quest;

public final class OlympiadSave extends Quest implements Runnable {

	public OlympiadSave() {
		super(-1, "tasks");

		ThreadPool.scheduleAtFixedRate(this, 900000, 1800000);
	}

	@Override
	public final void run() {
		if (Olympiad.getInstance().inCompPeriod()) {
			Olympiad.getInstance().saveOlympiadStatus();
			_log.info("Olympiad: Data updated successfully.");
		}
	}
}
