package sf.l2j.gameserver.scripting.quests;

import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.scripting.Quest;
import sf.l2j.gameserver.scripting.QuestState;

public class Q319_ScentOfDeath extends Quest {

	private static final String qn = "Q319_ScentOfDeath";

	// Item
	private static final int ZOMBIE_SKIN = 1045;

	public Q319_ScentOfDeath() {
		super(319, "Scent of Death");

		setItemsIds(ZOMBIE_SKIN);

		addStartNpc(30138); // Minaless
		addTalkId(30138);

		addKillId(20015, 20020);
	}

	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = event;
		QuestState st = player.getQuestState(qn);
		if (st == null) {
			return htmltext;
		}

		if (event.equalsIgnoreCase("30138-04.htm")) {
			st.setState(STATE_STARTED);
			st.set("cond", "1");
			st.playSound(QuestState.SOUND_ACCEPT);
		}

		return htmltext;
	}

	@Override
	public String onTalk(Npc npc, Player player) {
		QuestState st = player.getQuestState(qn);
		String htmltext = getNoQuestMsg();
		if (st == null) {
			return htmltext;
		}

		switch (st.getState()) {
			case STATE_CREATED:
				htmltext = (player.getLevel() < 11) ? "30138-02.htm" : "30138-03.htm";
				break;

			case STATE_STARTED:
				if (st.getInt("cond") == 1) {
					htmltext = "30138-05.htm";
				} else {
					htmltext = "30138-06.htm";
					st.takeItems(ZOMBIE_SKIN, -1);
					st.rewardItems(57, 3350);
					st.rewardItems(1060, 1);
					st.playSound(QuestState.SOUND_FINISH);
					st.exitQuest(true);
				}
				break;
		}
		return htmltext;
	}

	@Override
	public String onKill(Npc npc, Player player, boolean isPet) {
		QuestState st = checkPlayerCondition(player, npc, "cond", "1");
		if (st == null) {
			return null;
		}

		if (st.dropItems(ZOMBIE_SKIN, 1, 5, 200000)) {
			st.set("cond", "2");
		}

		return null;
	}
}
