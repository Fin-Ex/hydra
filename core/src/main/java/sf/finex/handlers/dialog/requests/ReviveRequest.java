/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers.dialog.requests;

import sf.finex.data.ReviveRequestData;
import sf.finex.handlers.IDialogRequest;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.EEffectFlag;

/**
 *
 * @author FinFan
 */
public class ReviveRequest implements IDialogRequest {

	@Override
	public Boolean handle(Player activeChar, Object... args) {
		final Player reanimator = (Player) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final boolean revivePet = (boolean) args[2];
		if (activeChar.getReviveRequest() != null) {
			// Resurrection has already been proposed.
			if (activeChar.getReviveRequest().isRevivePet()) {
				reanimator.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED);
			} else {
				if (activeChar.getReviveRequest().isRevivePet()) // A pet cannot be resurrected while it's owner is in the process of resurrecting.
				{
					reanimator.sendPacket(SystemMessageId.CANNOT_RES_PET2);
				} else // While a pet is attempting to resurrect, it cannot help in resurrecting its master.
				{
					reanimator.sendPacket(SystemMessageId.MASTER_CANNOT_RES);
				}
			}
		} else {
			activeChar.setReviveRequest(new ReviveRequestData(reanimator, activeChar));
			if ((activeChar.getReviveRequest().isRevivePet() && activeChar.getActiveSummon() != null && activeChar.getActiveSummon().isDead()) || (!activeChar.getReviveRequest().isRevivePet() && activeChar.isDead())) {
				if (activeChar.isPhoenixBlessed()) {
					activeChar.getReviveRequest().setRevivePower(100);
				} else if (activeChar.isAffected(EEffectFlag.CHARM_OF_COURAGE)) {
					activeChar.getReviveRequest().setRevivePower(0);
				} else {
					activeChar.getReviveRequest().setRevivePower(Formulas.calculateSkillResurrectRestorePercent(skill.getPower(), reanimator));
				}

				activeChar.getReviveRequest().setRevivePet(revivePet);

				if (activeChar.isAffected(EEffectFlag.CHARM_OF_COURAGE)) {
					activeChar.sendPacket(new ConfirmDlg(SystemMessageId.DO_YOU_WANT_TO_BE_RESTORED).addTime(60000));
					return Boolean.FALSE;
				}

				activeChar.sendPacket(new ConfirmDlg(SystemMessageId.RESSURECTION_REQUEST_BY_S1).addCharName(reanimator));
			}
		}

		return Boolean.TRUE;

	}

}
