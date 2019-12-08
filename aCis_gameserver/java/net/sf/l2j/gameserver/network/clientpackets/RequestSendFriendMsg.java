package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.L2FriendSay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Recieve Private (Friend) Message - 0xCC Format: c SS S: Message S: Receiving
 * Player
 *
 * @author Tempy
 */
public final class RequestSendFriendMsg extends L2GameClientPacket {

	private static final Logger CHAT_LOG = LoggerFactory.getLogger("chat");

	private String message;
	private String receiver;

	@Override
	protected void readImpl() {
		message = readS();
		receiver = readS();
	}

	@Override
	protected void runImpl() {
		if (message == null || message.isEmpty() || message.length() > 300) {
			return;
		}

		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final Player targetPlayer = World.getInstance().getPlayer(receiver);
		if (targetPlayer == null || !targetPlayer.getFriendList().contains(activeChar.getObjectId())) {
			activeChar.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			return;
		}

		if (Config.LOG_CHAT) {
			CHAT_LOG.info("{} -> {}: {}", activeChar, receiver, message);
		}

		targetPlayer.sendPacket(new L2FriendSay(activeChar.getName(), receiver, message));
	}
}
