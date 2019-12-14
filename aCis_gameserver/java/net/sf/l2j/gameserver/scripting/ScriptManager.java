package net.sf.l2j.gameserver.scripting;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class ScriptManager implements Runnable {

	private static final Logger _log = LoggerFactory.getLogger(ScriptManager.class.getName());

	public static ScriptManager getInstance() {
		return SingletonHolder._instance;
	}

	public static final int PERIOD = 5 * 60 * 1000; // 5 minutes

	private final List<Quest> _quests = new ArrayList<>();
	private final List<ScheduledQuest> _scheduled = new LinkedList<>();

	public ScriptManager() {
		load();
	}

	private final void load() {
		try {
			File f = new File("./data/xml/scripts.xml");
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);

			Node n = doc.getFirstChild();
			for (Node script = n.getFirstChild(); script != null; script = script.getNextSibling()) {
				if (!script.getNodeName().equalsIgnoreCase("script")) {
					continue;
				}

				final NamedNodeMap params = script.getAttributes();

				// Get path to the script.
				Node param = params.getNamedItem("path");
				if (param == null) {
					_log.warn("ScriptManager: The \"path\" is not defined.");
					continue;
				}
				final String path = param.getNodeValue();

				try {
					// Create the script.
					Quest instance = (Quest) Class.forName("net.sf.l2j.gameserver.scripting." + path).newInstance();

					// Add quest, script, AI or any other custom type of script.
					_quests.add(instance);

					// The script has been identified as a scheduled script, make proper checks and schedule the launch.
					if (instance instanceof ScheduledQuest) {
						// Get schedule parameter, when not exist, script is not scheduled.
						param = params.getNamedItem("schedule");
						if (param == null) {
							continue;
						}

						final String type = param.getNodeValue();

						// Get mandatory start parameter, when not exist, script is not scheduled.
						param = params.getNamedItem("start");
						if (param == null) {
							_log.warn("ScriptManager: Missing \"start\" parametr for \"" + path + "\".");
							continue;
						}
						final String start = param.getNodeValue();

						// Get optional end parameter, when not exist, script is one-event type.
						param = params.getNamedItem("end");
						String end = null;
						if (param != null) {
							end = param.getNodeValue();
						}

						// Schedule script, when successful, register it.
						if (((ScheduledQuest) instance).setSchedule(type, start, end)) {
							_scheduled.add(((ScheduledQuest) instance));
						}
					}
				} catch (ClassNotFoundException e) {
					_log.warn("ScriptManager: Script \"" + path + "\" not found.");
					continue;
				}
			}

			_log.info("ScriptManager: Loaded " + _quests.size() + " scripts, " + _scheduled.size() + " are scheduled.");
		} catch (Exception e) {
			_log.warn("ScriptManager: Error loading \"scripts.xml\" file, " + e);
		}

		ThreadPool.scheduleAtFixedRate(this, 0, PERIOD);
	}

	@Override
	public void run() {
		// each PERIOD
		final long next = System.currentTimeMillis() + PERIOD;

		// check all scheduled scripts
		for (ScheduledQuest script : _scheduled) {
			// when next action triggers in closest period, schedule the script action
			final long eta = next - script.getTimeNext();
			if (eta > 0) {
				ThreadPool.schedule(new Scheduler(script), PERIOD - eta);
			}
		}
	}

	private final class Scheduler implements Runnable {

		private final ScheduledQuest _script;

		protected Scheduler(ScheduledQuest script) {
			_script = script;
		}

		@Override
		public void run() {
			// notify script
			_script.notifyAndSchedule();

			// in case the next action is triggered before the resolution of ScriptManager, schedule the the action again
			final long eta = System.currentTimeMillis() + PERIOD - _script.getTimeNext();
			if (eta > 0) {
				ThreadPool.schedule(this, PERIOD - eta);
			}
		}
	}

	/**
	 * Returns the quest by given quest name.
	 *
	 * @param questName : The name of the quest.
	 * @return Quest : Quest to be returned, null if quest does not exist.
	 */
	public final Quest getQuest(String questName) {
		// Check all quests.
		for (Quest q : _quests) {
			// If quest found, return him.
			if (q.getName().equalsIgnoreCase(questName)) {
				return q;
			}
		}

		// Otherwise return null.
		return null;
	}

	/**
	 * Returns the quest by given quest id.
	 *
	 * @param questId : The id of the quest.
	 * @return Quest : Quest to be returned, null if quest does not exist.
	 */
	public final Quest getQuest(int questId) {
		// Check all quests.
		for (Quest q : _quests) {
			// If quest found, return him.
			if (q.getQuestId() == questId) {
				return q;
			}
		}

		// Otherwise return null.
		return null;
	}

	/**
	 * Returns the list of quests.
	 *
	 * @return List<Quest> : List of quest.
	 */
	public final List<Quest> getQuests() {
		return _quests;
	}

	private static class SingletonHolder {

		protected static final ScriptManager _instance = new ScriptManager();
	}
}
