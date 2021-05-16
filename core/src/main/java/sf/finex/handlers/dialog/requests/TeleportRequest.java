/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers.dialog.requests;

import sf.finex.handlers.IDialogRequest;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author FinFan
 */
public class TeleportRequest implements IDialogRequest {

	@Override
	public Boolean handle(Player requester, Object... args) {
		final Player target = (Player) args[0];
		final L2Skill skill = (L2Skill) args[1];

		if (target.getSummonTargetRequest() != null && requester != null) {
			return Boolean.FALSE;
		}

		target.setSummonTargetRequest(requester);
		target.setSummonSkillRequest(skill);
		return Boolean.TRUE;
	}

}
