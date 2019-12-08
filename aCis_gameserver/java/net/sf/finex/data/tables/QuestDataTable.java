/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data.tables;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.StorageTable;
import net.sf.finex.data.QuestData;

/**
 *
 * @author FinFan
 */
@Slf4j
public class QuestDataTable extends StorageTable {

	@Getter private static final QuestDataTable instance = new QuestDataTable();
	
	private final List<QuestData> holder = new ArrayList<>();

	public QuestDataTable() {
		load();
	}

	@Override
	public void reload() {
		holder.clear();
		load();
		log.info("All quests data was reloaded!");
	}

	@Override
	protected void load() {
		final Gson gson = new Gson();
		final File file = new File("data/json/quest_data.json");
		try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
			holder.addAll(gson.fromJson(reader, new TypeToken<List<QuestData>>() {}.getType()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		log.info("Loaded: {} quest templates.", holder.size());
	}

	@Override
	public QuestData get(int identifier) {
		for (QuestData data : holder) {
			if (data.getId() == identifier) {
				return data;
			}
		}

		return null;
	}

	@Override
	public List<QuestData> holder() {
		return holder;
	}

}
