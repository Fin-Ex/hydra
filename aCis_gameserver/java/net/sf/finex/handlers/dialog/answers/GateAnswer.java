/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.answers;

import org.slf4j.LoggerFactory;

import net.sf.finex.handlers.IDialogAnswer;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Door;

/**
 *
 * @author FinFan
 */
public class GateAnswer implements IDialogAnswer {

	public final boolean open;

	public GateAnswer(boolean open) {
		this.open = open;
	}

	@Override
	public void handle(Player activeChar, int answer, int requesterId) {
		final Door door = activeChar.getRequestedGate();
		if (door == null) {
			return;
		}

		if (answer == 1 && activeChar.getTarget() == door) {
			if (open) {
				door.openMe();
			} else {
				door.closeMe();
			}
		}

		activeChar.setRequestedGate(null);
	}

}
