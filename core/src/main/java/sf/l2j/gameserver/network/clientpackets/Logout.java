package sf.l2j.gameserver.network.clientpackets;

import sf.finex.model.generator.quest.RandomQuestManager;
import sf.l2j.gameserver.instancemanager.SevenSignsFestival;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.events.OnLogout;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;

public final class Logout extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		if (player.getActiveEnchantItem() != null || player.isLocked()) {
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (player.isInsideZone(ZoneId.NO_RESTART)) {
			player.sendPacket(SystemMessageId.NO_LOGOUT_HERE);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (AttackStanceTaskManager.getInstance().isInAttackStance(player)) {
			player.sendPacket(SystemMessageId.CANT_LOGOUT_WHILE_FIGHTING);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (player.isFestivalParticipant() && SevenSignsFestival.getInstance().isFestivalInitialized()) {
			player.sendPacket(SystemMessageId.NO_LOGOUT_HERE);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		RandomQuestManager.getInstance().getEventBus().notify(new OnLogout(player));
		player.removeFromBossZone();
		player.logout();
	}
}
