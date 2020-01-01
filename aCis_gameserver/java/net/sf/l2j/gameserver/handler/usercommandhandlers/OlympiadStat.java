package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Support for /olympiadstat command Added by kamy
 */
public class OlympiadStat implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		109
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		if (!activeChar.isNoble()) {
			activeChar.sendPacket(SystemMessageId.NOBLESSE_ONLY);
			return;
		}

		int nobleObjId = activeChar.getObjectId();
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.THE_CURRENT_RECORD_FOR_THIS_OLYMPIAD_SESSION_IS_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_HAVE_EARNED_S4_OLYMPIAD_POINTS);
		sm.addNumber(Olympiad.getInstance().getCompetitionDone(nobleObjId));
		sm.addNumber(Olympiad.getInstance().getCompetitionWon(nobleObjId));
		sm.addNumber(Olympiad.getInstance().getCompetitionLost(nobleObjId));
		sm.addNumber(Olympiad.getInstance().getNoblePoints(nobleObjId));
		activeChar.sendPacket(sm);
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
