package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class Time implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		77
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		final int hour = GameTimeTaskManager.getInstance().getGameHour();
		final int minute = GameTimeTaskManager.getInstance().getGameMinute();

		final String min = ((minute < 10) ? "0" : "") + minute;

		activeChar.sendPacket(SystemMessage.getSystemMessage((GameTimeTaskManager.getInstance().isNight()) ? SystemMessageId.TIME_S1_S2_IN_THE_NIGHT : SystemMessageId.TIME_S1_S2_IN_THE_DAY).addNumber(hour).addString(min));
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
