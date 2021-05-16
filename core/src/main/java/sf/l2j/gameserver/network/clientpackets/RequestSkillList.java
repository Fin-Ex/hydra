package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;

public final class RequestSkillList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player cha = getClient().getActiveChar();
		if (cha == null) {
			return;
		}

		cha.sendSkillList();
	}
}
