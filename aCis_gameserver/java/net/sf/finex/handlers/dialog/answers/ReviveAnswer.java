/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.answers;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.ReviveRequestData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.finex.handlers.IDialogAnswer;

/**
 *
 * @author FinFan
 */
public class ReviveAnswer implements IDialogAnswer {

	@Override
	public void handle(Player player, int answer, int requesterId) {
		final ReviveRequestData reviveRequest = player.getReviveRequest();
		if (reviveRequest == null || (!player.isDead() && !reviveRequest.isRevivePet()) || (reviveRequest.isRevivePet() && player.getActiveSummon() != null && !player.getActiveSummon().isDead())) {
			return;
		}

		if (answer == 0 && player.isPhoenixBlessed()) {
			player.stopPhoenixBlessing(null);
		} else if (answer == 1) {
			if (!reviveRequest.isRevivePet()) {
				if (reviveRequest.getRevivePower() != 0) {
					player.doRevive(reviveRequest.getRevivePower());
				} else {
					player.doRevive();
				}
			} else if (player.getActiveSummon() != null) {
				if (reviveRequest.getRevivePower() != 0) {
					player.getActiveSummon().doRevive(reviveRequest.getRevivePower());
				} else {
					player.getActiveSummon().doRevive();
				}
			}
		}
		player.setReviveRequest(null);
	}

}
