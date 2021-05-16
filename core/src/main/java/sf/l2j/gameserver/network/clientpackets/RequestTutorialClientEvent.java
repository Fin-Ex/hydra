package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.scripting.QuestState;

public class RequestTutorialClientEvent extends L2GameClientPacket {

	int eventId;

	@Override
	protected void readImpl() {
		eventId = readD();
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		QuestState qs = player.getQuestState("Tutorial");
		if (qs != null) {
			qs.getQuest().notifyEvent("CE" + eventId + "", null, player);
		}
	}
}
