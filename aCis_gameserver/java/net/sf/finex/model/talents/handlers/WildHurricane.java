/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents.handlers;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public class WildHurricane implements TalentHandler {

	private static final int WHIRLWIND = 36;
	
	@Override
	public Integer invoke(Object... args) {
		final int reuseDelay = (int) args[0];
		final int targetCount = (int) args[1];
		return Math.max(0, reuseDelay - (targetCount * 3000));
	}

	public static final boolean validate(Creature activeChar, L2Skill skill) {
		if (!activeChar.isPlayer()) {
			return false;
		}

		return skill.getId() == WHIRLWIND && activeChar.getPlayer().hasTalent(SkillTable.FrequentTalent.WILD_HURRICANE);
	}
}
