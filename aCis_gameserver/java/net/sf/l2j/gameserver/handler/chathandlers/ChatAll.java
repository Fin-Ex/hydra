package net.sf.l2j.gameserver.handler.chathandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatAll implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		0
	};

	@Override
	public void invoke(Object... args) {
		final int type = (int) args[0];
		final Player activeChar = (Player) args[1];
		final String params = (String) args[2];
		final String text = (String) args[3];
		if (!FloodProtectors.performAction(activeChar.getClient(), Action.GLOBAL_CHAT)) {
			return;
		}

		final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
		for (Player player : activeChar.getKnownTypeInRadius(Player.class, 1250)) {
			if (!BlockList.isBlocked(player, activeChar)) {
				player.sendPacket(cs);
			}
		}
		activeChar.sendPacket(cs);
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
