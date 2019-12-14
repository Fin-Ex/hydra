/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.tables.GladiatorRankTable;
import net.sf.finex.data.tables.RecipeTable;
import net.sf.finex.data.tables.TalentBranchTable;
import net.sf.finex.data.tables.TalentTable;
import net.sf.finex.model.generator.quest.RandomQuestManager;
import net.sf.l2j.Config;

/**
 *
 * @author FinFan
 */
@Slf4j
public class FinexLoader {

	//test
	@Getter
	private static final FinexLoader instance = new FinexLoader();

	public FinexLoader() {
		RecipeTable.getInstance();
		TalentTable.getInstance();
		TalentBranchTable.getInstance();
		if (Config.RANDOM_QUEST_GENERATOR_ON) {
			// spawn all tables
			RandomQuestManager.getInstance().generateQuests();
		} else {
			log.info("Random Quest Generate system is OFF.");
		}
		GladiatorRankTable.getInstance();
	}
}
