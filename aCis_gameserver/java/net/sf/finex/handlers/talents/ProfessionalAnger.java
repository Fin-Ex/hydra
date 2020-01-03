/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import net.sf.finex.model.talents.TalentHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public class ProfessionalAnger implements TalentHandler {

	private static final short WAR_CRY = 78;
	
	@Override
	public Integer invoke(Object... args) {
		return 2;
	}
	
	public static final boolean validate(Player player, L2Skill skill) {
		return skill.getId() == WAR_CRY && player.hasTalent(12);
	}
	
}
