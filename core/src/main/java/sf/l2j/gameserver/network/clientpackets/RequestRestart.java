package sf.l2j.gameserver.network.clientpackets;

import sf.finex.model.generator.quest.RandomQuestManager;
import sf.l2j.gameserver.instancemanager.SevenSignsFestival;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.events.OnLogout;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.network.L2GameClient;
import sf.l2j.gameserver.network.L2GameClient.GameClientState;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.CharSelectInfo;
import sf.l2j.gameserver.network.serverpackets.RestartResponse;
import sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;

public final class RequestRestart extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		if (player.getActiveEnchantItem() != null || player.isLocked() || player.isInStoreMode()) {
			sendPacket(RestartResponse.valueOf(false));
			return;
		}

		if (player.isInsideZone(ZoneId.NO_RESTART)) {
			player.sendPacket(SystemMessageId.NO_RESTART_HERE);
			sendPacket(RestartResponse.valueOf(false));
			return;
		}

		if (AttackStanceTaskManager.getInstance().isInAttackStance(player)) {
			player.sendPacket(SystemMessageId.CANT_RESTART_WHILE_FIGHTING);
			sendPacket(RestartResponse.valueOf(false));
			return;
		}

		if (player.isFestivalParticipant() && SevenSignsFestival.getInstance().isFestivalInitialized()) {
			player.sendPacket(SystemMessageId.NO_RESTART_HERE);
			sendPacket(RestartResponse.valueOf(false));
			return;
		}

		player.removeFromBossZone();

		final L2GameClient client = getClient();

		// Logout event
		RandomQuestManager.getInstance().getEventBus().notify(new OnLogout(player));

		// detach the client from the char so that the connection isnt closed in the deleteMe
		player.setClient(null);

		// removing player from the world
		player.deleteMe();

		client.setActiveChar(null);
		client.setState(GameClientState.AUTHED);

		sendPacket(RestartResponse.valueOf(true));

		// send char list
		final CharSelectInfo cl = new CharSelectInfo(client.getAccountName(), client.getSessionId().playOkID1);
		sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
	}
}