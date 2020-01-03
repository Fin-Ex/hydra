/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import net.sf.finex.model.talents.TalentHandler;
import net.sf.finex.data.tables.GladiatorRankTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author finfan
 */
public class Challenger implements TalentHandler {

	@Override
	public Double invoke(Object... args) {
		return 2.0;
	}

	public static final boolean validate(Player player) {
		return GladiatorRankTable.getInstance().isOneOfTen(player) && player.hasTalent(SkillTable.FrequentTalent.CHALLENGER);
	}
}
