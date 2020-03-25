/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.HunterCardInstance;

/**
 *
 * @author finfan
 */
public class HxHQuest implements IHandler {

	private static final Integer[] COMMANDS = {
		116
	};

	@Override
	public Integer[] commands() {
		return COMMANDS;
	}

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];

		final HunterCardInstance card = activeChar.getInventory().getHunterCardInstance();
		if (card == null) {
			activeChar.sendMessage("You dont have a Hutner Card ID!");
			return;
		}
		
		card.getQuest(activeChar);
	}
}
