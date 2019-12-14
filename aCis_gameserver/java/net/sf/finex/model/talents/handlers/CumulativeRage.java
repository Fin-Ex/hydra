/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents.handlers;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author finfan
 */
public class CumulativeRage implements TalentHandler {

	@Override
	public Integer invoke(Object... args) {
		final Player caster = (Player) args[0];
		final int damage = (int) args[1];
		final double modifier = 0.7 + 0.3 * caster.getCharges();
		return (int) (damage * modifier);
	}

	public static final boolean validate(Player player) {
		return player.hasTalent(SkillTable.FrequentTalent.CUMULATIVE_RAGE);
	}
}
