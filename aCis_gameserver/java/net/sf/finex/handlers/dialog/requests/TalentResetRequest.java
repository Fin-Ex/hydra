/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.requests;

import net.sf.finex.handlers.IDialogRequest;
import net.sf.finex.model.talents.LineagePointsManager;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;

/**
 *
 * @author FinFan
 */
public class TalentResetRequest implements IDialogRequest {

	@Override
	public Boolean handle(Player player, Object... args) {
		// check for reseting points (if we have all of them)
		if(player.getLineagePoints() == Config.LINEAGE_REACH_LEVEL.length) {
			player.sendPacket(SystemMessageId.NOTHING_TO_RESET);
			return Boolean.FALSE;
		}
		
		// check the price
		if(LineagePointsManager.getInstance().validateReset(player, false) == 0) {
			return Boolean.FALSE;
		}
		
		// send packet
		final ConfirmDlg dlg;
		if(Config.TALENT_RESET_ID <= 0 && Config.TALENT_RESET_PRICE <= 0) {
			// FREE
			dlg = new ConfirmDlg(SystemMessageId.DO_YOU_WANT_TO_RESET_ALL_YOUR_MASTERIES);
		} else if(Config.TALENT_RESET_ID > 0 && Config.TALENT_RESET_PRICE <= 0) {
			// ITEM x1
			dlg = new ConfirmDlg(SystemMessageId.DO_YOU_WANT_TO_RESET_ALL_YOUR_MASTERIES_FOR_S1_S2);
			dlg.addNumber(1);
			final Item item = ItemTable.getInstance().getTemplate(Config.TALENT_RESET_ID);
			if (item == null) {
				throw new NullPointerException("Item with ID " + Config.TALENT_RESET_ID + " doesnt exists.");
			}
			dlg.addString(item.getName());
		} else if(Config.TALENT_RESET_ID <= 0 && Config.TALENT_RESET_PRICE > 0) {
			// SP
			dlg = new ConfirmDlg(SystemMessageId.DO_YOU_WANT_TO_RESET_ALL_YOUR_MASTERIES_FOR_S1_S2);
			dlg.addNumber(player.getLineageResetPrice());
			dlg.addString("SP");
		} else {
			// ITEM xN
			dlg = new ConfirmDlg(SystemMessageId.DO_YOU_WANT_TO_RESET_ALL_YOUR_MASTERIES_FOR_S1_S2);
			dlg.addNumber(player.getLineageResetPrice());
			final Item item = ItemTable.getInstance().getTemplate(Config.TALENT_RESET_ID);
			if (item == null) {
				throw new NullPointerException("Item with ID " + Config.TALENT_RESET_ID + " doesnt exists.");
			}
			dlg.addString(item.getName());
		}
		
		player.sendPacket(dlg);
		return Boolean.TRUE;
	}
	
}
