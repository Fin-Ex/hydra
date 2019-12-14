/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.finex.model.classes.Gladiator;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.base.ClassId;

/**
 *
 * @author finfan
 */
public class Challenge implements IUserCommandHandler {

	private static final int[] COMMANDS = {
		115
	};

	@Override
	public boolean useUserCommand(int id, Player activeChar) {
		if (!activeChar.getClassId().equalsOrChildOf(ClassId.Gladiator)) {
			return false;
		}

		final Gladiator glad = activeChar.getComponent(Gladiator.class);
		glad.setInDuelMode(!glad.isInDuelMode());
		if (!glad.isInDuelMode()) {
			activeChar.sendMessage("Duel mode deactivated.");
		} else {
			activeChar.sendMessage("Duel mode activated.");
		}
		return true;
	}

	@Override
	public int[] getUserCommandList() {
		return COMMANDS;
	}

}
