/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.dialog.requests;

import org.slf4j.LoggerFactory;

import net.sf.finex.handlers.IDialogRequest;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

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
