package net.sf.l2j.gameserver.handler.chathandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatShout implements IChatHandler {

	private static final int[] COMMAND_IDS
			= {
				1
			};

	@Override
	public void handleChat(int type, Player activeChar, String target, String text) {
		if (!FloodProtectors.performAction(activeChar.getClient(), Action.GLOBAL_CHAT)) {
			return;
		}

		final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
		final int region = MapRegionTable.getInstance().getMapRegion(activeChar.getX(), activeChar.getY());

		for (Player player : World.getInstance().getPlayers()) {
			if (!BlockList.isBlocked(player, activeChar) && region == MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY())) {
				player.sendPacket(cs);
			}
		}
	}

	@Override
	public int[] getChatTypeList() {
		return COMMAND_IDS;
	}
}
