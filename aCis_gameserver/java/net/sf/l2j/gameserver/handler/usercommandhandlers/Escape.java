package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;

public class Escape implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		52
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		if (activeChar.isCastingNow() || activeChar.isSitting() || activeChar.isMovementDisabled() || activeChar.isOutOfControl() || activeChar.isInOlympiadMode() || activeChar.isInObserverMode() || activeChar.isFestivalParticipant() || activeChar.isInJail() || ZoneManager.getInstance().getZone(activeChar, L2BossZone.class) != null) {
			activeChar.sendPacket(SystemMessageId.NO_UNSTUCK_PLEASE_SEND_PETITION);
			return;
		}

		activeChar.stopMove(null);

		// Official timer 5 minutes, for GM 1 second
		if (activeChar.isGM()) {
			activeChar.doCast(SkillTable.getInstance().getInfo(2100, 1), false);
		} else {
			activeChar.sendPacket(new PlaySound("systemmsg_e.809"));
			activeChar.sendPacket(SystemMessageId.STUCK_TRANSPORT_IN_FIVE_MINUTES);

			activeChar.doCast(SkillTable.getInstance().getInfo(2099, 1), false);
		}
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
