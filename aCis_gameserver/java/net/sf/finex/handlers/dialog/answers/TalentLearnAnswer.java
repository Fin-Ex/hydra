/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.answers;

import net.sf.finex.dao.PlayerLineageDao;
import net.sf.finex.data.TalentData;
import net.sf.finex.data.tables.TalentTable;
import net.sf.finex.handlers.IDialogAnswer;
import net.sf.finex.model.talents.LineageCommandHandler;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author FinFan
 */
public class TalentLearnAnswer implements IDialogAnswer {

	@Override
	public void handle(Player activeChar, int answer, int talentId) {
		if (answer == 1) {
			if (activeChar.getLineagePoints() == 0) {
				activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_LINEAGE_POINTS);
				return;
			}

			final TalentData talent = TalentTable.getInstance().get(talentId);
			if (talent.getRequiredSkill() > 0 && activeChar.getSkill(talent.getRequiredSkill()) == null) {
				activeChar.sendMessage("you need learn skill " + talent.getRequiredSkill());
				return;
			}
			
			if (talent.getRequiredLevel() > 0 && activeChar.getLevel() < talent.getRequiredLevel()) {
				activeChar.sendMessage("you need level " + talent.getRequiredLevel());
				return;
			}

			if (talent.getRequiredTalent() > 0 && activeChar.hasTalent(talent.getRequiredTalent())) {
				activeChar.sendMessage("you need talent " + talent.getRequiredTalent());
				return;
			}

			activeChar.addSkill(SkillTable.getInstance().getInfo(talent.getSkillId(), 1), true);
			activeChar.sendSkillList();
			activeChar.setLineagePoints(activeChar.getLineagePoints() - 1);
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LP_WAS_CONSUMED).addNumber(1));
			PlayerLineageDao.update(activeChar);
		}
		LineageCommandHandler.showTalentList(activeChar);
	}

}
