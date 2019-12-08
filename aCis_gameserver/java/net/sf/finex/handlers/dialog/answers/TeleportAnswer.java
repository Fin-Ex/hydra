/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.answers;

import org.slf4j.LoggerFactory;

import net.sf.finex.handlers.IDialogAnswer;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public class TeleportAnswer implements IDialogAnswer {

	@Override
	public void handle(Player activeChar, int answer, int requesterId) {
		final Player target = activeChar.getSummonTargetRequest();
		if (target == null) {
			return;
		}

		if (answer == 1 && target.getObjectId() == requesterId) {
			Player.teleToTarget(activeChar, target, activeChar.getSummonSkillRequest());
		}

		activeChar.setSummonTargetRequest(null);
		activeChar.setSummonSkillRequest(null);
	}

}
