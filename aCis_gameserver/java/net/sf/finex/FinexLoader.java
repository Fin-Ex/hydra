/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.tables.GladiatorRankTable;
import net.sf.finex.data.tables.RecipeTable;
import net.sf.finex.data.tables.TalentBranchTable;
import net.sf.finex.data.tables.TalentTable;
import net.sf.finex.model.GLT.GLTController;
import net.sf.finex.model.GLT.GLTSettings;
import net.sf.finex.model.generator.quest.RandomQuestManager;
import net.sf.finex.model.items.ItemData;
import net.sf.finex.model.items.table.ItemHolder;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.item.kind.Item;

/**
 *
 * @author FinFan
 */
@Slf4j
public class FinexLoader {

	@Getter private static final FinexLoader instance = new FinexLoader();

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
		if (GLTSettings.ACTIVATED) {
			GLTController.getInstance().restart();
		}

		ITEMS_ToJson();
		ItemHolder.getInstance();
	}
	
	private void ITEMS_ToJson() {
		final Map<String, List<ItemData>> map = new HashMap<>();
		for (Item item : ItemTable.getInstance().getTemplates()) {
			if (item == null) {
				continue;
			}

			final String loadName = item.getLoadName();
			if(!map.containsKey(loadName)) {
				map.put(loadName, new ArrayList<>());
			}
			
			map.get(loadName).add(ItemData.create(item));
		}
		
		for (Map.Entry<String, List<ItemData>> next : map.entrySet()) {
			final String key = next.getKey();
			final List<ItemData> list = next.getValue();
			final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			write("data/json/items/" + key + ".json", gson.toJson(list));
		}
	}
	
	private <T> void write(String path, String value) {
		try {
			final File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}

			try (FileWriter writer = new FileWriter(f, true)) {
				writer.write(value);
			}
		} catch (IOException e) {
			log.error("Error when record the file", e);
		}
	}
}
