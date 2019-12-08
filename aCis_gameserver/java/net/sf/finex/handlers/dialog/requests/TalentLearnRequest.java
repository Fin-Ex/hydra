/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.requests;

import net.sf.finex.data.TalentData;
import net.sf.finex.data.tables.TalentTable;
import net.sf.finex.handlers.IDialogRequest;
import net.sf.finex.model.talents.LineageCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import static net.sf.l2j.gameserver.network.SystemMessageId.S1_ALREADY_LEARNED;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author FinFan
 */
public class TalentLearnRequest implements IDialogRequest {

	@Override
	public Boolean handle(Player player, Object... args) {
		final TalentData talent = (TalentData) args[0];
		
		if(player.getLevel() < talent.getRequiredLevel()) {
			player.sendPacket(SystemMessageId.YOUR_LEVEL_IS_TOO_LOW);
			LineageCommandHandler.showTalentInfo(player, talent.getId());
			return Boolean.FALSE;
		}
		
		if(talent.getRequiredTalent() > 0 && !player.hasTalent(talent.getRequiredTalent())) {
			final TalentData reqTalent = TalentTable.getInstance().get(talent.getRequiredTalent());
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_MUST_LEARN_S1_BEFORE_LEARN_S2).addString(reqTalent.getName()).addString(talent.getName()));
			LineageCommandHandler.showTalentInfo(player, talent.getId());
			return Boolean.FALSE;
		}
		
		if(player.hasTalent(TalentTable.getInstance().get(talent.getId()))) {
			player.sendPacket(SystemMessage.getSystemMessage(S1_ALREADY_LEARNED).addString(talent.getName()));
			LineageCommandHandler.showTalentInfo(player, talent.getId());
			return Boolean.FALSE;
		}
		
		if(player.getLineagePoints() <= 0) {
			player.sendPacket(SystemMessageId.NOT_ENOUGH_LINEAGE_POINTS);
			return Boolean.FALSE;
		}
		
		player.sendPacket(new ConfirmDlg(SystemMessageId.DO_YOU_REALY_WANT_TO_LEARN_S1_TALENT)
				.addTime(30000)
				.addString(talent.getName())
				.addRequesterId(talent.getId()));
		return Boolean.TRUE;
	}
	
}
