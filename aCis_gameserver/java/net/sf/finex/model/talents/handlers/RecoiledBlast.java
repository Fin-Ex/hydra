/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents.handlers;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public class RecoiledBlast implements TalentHandler {

	private static final short SONIC_BLASTER = 6;
	
	@Override
	public Boolean invoke(Object... args) {
		return Boolean.TRUE;
	}
	
	private void recoil() {
		
	}
	
	public static final boolean validate(Player player, L2Skill skill) {
		return skill.getId() == SONIC_BLASTER && player.hasTalent(13);
	}
	
}
