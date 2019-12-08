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
import net.sf.finex.data.TalentData;

/**
 *
 * @author FinFan
 */
@Slf4j
public class TalentTable extends StorageTable {

	@Getter private static final TalentTable instance = new TalentTable();

	private final List<TalentData> holder = new ArrayList<>();
	
	private TalentTable() {
		load();
	}
	
	@Override
	public void reload() {
		holder.clear();
		load();
		log.info("Talents Reloaded!");
	}

	@Override
	protected void load() {
		final Gson gson = new Gson();
		final File file = new File("data/json/talents/");
		for(File f : file.listFiles()) {
			final char symbol = f.getName().charAt(0);
			if(Character.isDigit(symbol)) {
				try (Reader reader = new InputStreamReader(new FileInputStream(f))) {
					holder.addAll(gson.fromJson(reader, new TypeToken<List<TalentData>>() {
					}.getType()));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		log.info("Loaded: {} talent templates.", holder.size());
	}

	@Override
	public TalentData get(int identifier) {
		for (TalentData data : holder) {
			if (data.getId() == identifier) {
				return data;
			}
		}

		return null;
	}

	@Override
	public List<TalentData> holder() {
		return holder;
	}
	
	public TalentData getBySkillId(int skillId) {
		for (TalentData data : holder) {
			if (data.getSkillId() == skillId) {
				return data;
			}
		}

		return null;
	}
}
