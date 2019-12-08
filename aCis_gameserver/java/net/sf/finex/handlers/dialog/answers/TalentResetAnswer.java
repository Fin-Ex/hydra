/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.answers;

import net.sf.finex.handlers.IDialogAnswer;
import net.sf.finex.model.talents.LineagePointsManager;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public class TalentResetAnswer implements IDialogAnswer {

	@Override
	public void handle(Player activeChar, int answer, int requesterId) {
		if (answer == 1) {
			final byte result = LineagePointsManager.getInstance().validateReset(activeChar, true);
			if (result != 0) {
				// pay for reseting and update DB
				LineagePointsManager.getInstance().resetTalents(activeChar, result == -1);
			}
		}
	}

}
