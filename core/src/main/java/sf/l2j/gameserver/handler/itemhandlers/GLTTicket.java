/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.handler.itemhandlers;

import sf.finex.model.GLT.GLTController;
import sf.finex.model.GLT.GLTParticipant;
import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;

/**
 *
 * @author finfan
 */
public class GLTTicket implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!playable.isPlayer()) {
			return;
		}

		final Player activeChar = (Player) playable;
		if(GLTController.getInstance().isParticipate(activeChar)) {
			final GLTParticipant participant = GLTController.getInstance().getParticipant(activeChar);
			final int huntNumber = participant.getHuntNumber();
			if(huntNumber > 0) {
				activeChar.sendMessage("[GLT Service] Your Number: " + participant.getOwnNumber());
				activeChar.sendMessage("[GLT Service] Hunt Number: " + huntNumber);
			} else {
				activeChar.sendMessage("[GLT Service] In the column \"huntable target\" - empty... ");
			}
		}
	}
	
}
