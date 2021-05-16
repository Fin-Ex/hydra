/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers.talents;

import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.model.actor.Player;
import sf.finex.model.talents.ITalentHandler;

/**
 *
 * @author finfan
 */
public class CumulativeRage implements ITalentHandler {

	@Override
	public Integer invoke(Object... args) {
		final Player caster = (Player) args[0];
		final int damage = (int) args[1];
		final double modifier = caster.getCharges() / 10. + 1;
		return (int) (damage * modifier);
	}

	public static final boolean validate(Player player) {
		return player.hasTalent(SkillTable.FrequentTalent.CUMULATIVE_RAGE);
	}
}
