/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers.talents;

import sf.finex.data.tables.GladiatorRankTable;
import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.model.actor.Player;
import sf.finex.model.talents.ITalentHandler;

/**
 *
 * @author finfan
 */
public class Challenger implements ITalentHandler {

	@Override
	public Double invoke(Object... args) {
		return 2.0;
	}

	public static final boolean validate(Player player) {
		return GladiatorRankTable.getInstance().isOneOfTen(player) && player.hasTalent(SkillTable.FrequentTalent.CHALLENGER);
	}
}
