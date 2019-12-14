package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;

public final class RequestPrivateStoreQuitBuy extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		player.forceStandUp();
	}
}
