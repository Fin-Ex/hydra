/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers.dialog.answers;

import sf.finex.handlers.IDialogAnswer;
import sf.l2j.gameserver.instancemanager.CoupleManager;
import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.instance.WeddingManagerNpc;

/**
 *
 * @author FinFan
 */
public class EngageAnswer implements IDialogAnswer {

	@Override
	public void handle(Player activeChar, int answer, int requesterId) {
		if (!activeChar.isUnderMarryRequest() || requesterId == 0) {
			return;
		}

		final Player requester = World.getInstance().getPlayer(requesterId);
		if (requester != null) {
			if (answer == 1) {
				// Create the couple
				CoupleManager.getInstance().addCouple(requester, activeChar);

				// Then "finish the job"
				WeddingManagerNpc.justMarried(requester, activeChar);
			} else {
				activeChar.setUnderMarryRequest(false);
				activeChar.sendMessage("You declined your partner's marriage request.");

				requester.setUnderMarryRequest(false);
				requester.sendMessage("Your partner declined your marriage request.");
			}
		}
	}

}
