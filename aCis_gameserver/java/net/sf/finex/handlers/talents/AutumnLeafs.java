/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import net.sf.finex.model.talents.ITalentHandler;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public class AutumnLeafs implements ITalentHandler {

	@Override
	public Boolean invoke(Object... args) {
		final Player player = (Player) args[0];
		player.setCurrentCp(player.getMaxCp());
		player.sendMessage("Talent Autumn Leaf's restores your entire CP additionally!");
		return Boolean.TRUE;
	}
	
	public static final boolean validate(Creature caster, L2Skill skill) {
		if(!caster.isPlayer()) {
			return false;
		}
		
		return skill.getId() == 181 && caster.getPlayer().hasTalent(SkillTable.FrequentTalent.AUTUMN_LEAFS);
	}
}
