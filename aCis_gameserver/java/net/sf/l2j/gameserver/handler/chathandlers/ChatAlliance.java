package net.sf.l2j.gameserver.handler.chathandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatAlliance implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		9
	};

	@Override
	public void invoke(Object... args) {
		final int type = (int) args[0];
		final Player activeChar = (Player) args[1];
		final String params = (String) args[2];
		final String text = (String) args[3];
		if (activeChar.getClan() == null || activeChar.getClan().getAllyId() == 0) {
			return;
		}

		activeChar.getClan().broadcastToOnlineAllyMembers(new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text));
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
