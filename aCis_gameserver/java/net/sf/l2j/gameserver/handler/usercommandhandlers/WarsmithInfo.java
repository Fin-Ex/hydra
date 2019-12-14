/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import org.slf4j.LoggerFactory;

import net.sf.finex.enums.EUIEventType;
import net.sf.finex.model.classes.Warsmith;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public class WarsmithInfo implements IUserCommandHandler {

	private static final int[] COMMANDS = {
		114
	};

	@Override
	public boolean useUserCommand(int id, Player activeChar) {
		if (!activeChar.hasComponent(Warsmith.class)) {
			return false;
		}

		activeChar.getComponent(Warsmith.class).showHtml(EUIEventType.INFO);
		return true;
	}

	@Override
	public int[] getUserCommandList() {
		return COMMANDS;
	}

}
