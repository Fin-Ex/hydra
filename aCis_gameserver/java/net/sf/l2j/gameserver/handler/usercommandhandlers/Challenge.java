/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.finex.model.classes.Gladiator;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.base.ClassId;

/**
 *
 * @author finfan
 */
public class Challenge implements IHandler {

	private static final Integer[] COMMANDS = {
		115
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		if (!activeChar.getClassId().equalsOrChildOf(ClassId.Gladiator)) {
			return;
		}

		final Gladiator glad = activeChar.getComponent(Gladiator.class);
		if (glad == null) {
			return;
		}

		if (activeChar.isInDuel()) {
			return;
		}

		glad.setInDuelMode(!glad.isInDuelMode());
		if (!glad.isInDuelMode()) {
			activeChar.sendMessage("Duel mode deactivated.");
		} else {
			activeChar.sendMessage("Duel mode activated.");
		}
	}

	@Override
	public Integer[] commands() {
		return COMMANDS;
	}

}
