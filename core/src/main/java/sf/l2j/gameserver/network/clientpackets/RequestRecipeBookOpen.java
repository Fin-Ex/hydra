package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;

public final class RequestRecipeBookOpen extends L2GameClientPacket {

	private boolean _isDwarvenCraft;

	@Override
	protected void readImpl() {
		_isDwarvenCraft = (readD() == 0);
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.isCastingNow() || activeChar.isAllSkillsDisabled()) {
			activeChar.sendPacket(SystemMessageId.NO_RECIPE_BOOK_WHILE_CASTING);
			return;
		}

		activeChar.requestBookOpen(_isDwarvenCraft);
	}
}
