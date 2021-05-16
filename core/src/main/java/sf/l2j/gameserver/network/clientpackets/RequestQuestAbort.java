package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.scripting.Quest;
import sf.l2j.gameserver.scripting.QuestState;
import sf.l2j.gameserver.scripting.ScriptManager;

public final class RequestQuestAbort extends L2GameClientPacket {

	private int _questId;

	@Override
	protected void readImpl() {
		_questId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final Quest qe = ScriptManager.getInstance().getQuest(_questId);
		if (qe == null) {
			return;
		}

		final QuestState qs = activeChar.getQuestState(qe.getName());
		if (qs != null) {
			qs.exitQuest(true);
		}
	}
}
