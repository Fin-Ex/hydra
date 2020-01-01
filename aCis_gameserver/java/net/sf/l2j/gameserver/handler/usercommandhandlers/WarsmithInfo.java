/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.usercommandhandlers;


import net.sf.finex.enums.EUIEventType;
import net.sf.finex.model.classes.Warsmith;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public class WarsmithInfo implements IHandler {

	private static final Integer[] COMMANDS = {
		114
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		if (!activeChar.hasComponent(Warsmith.class)) {
			return;
		}

		activeChar.getComponent(Warsmith.class).showHtml(EUIEventType.INFO);
	}

	@Override
	public Integer[] commands() {
		return COMMANDS;
	}

}
