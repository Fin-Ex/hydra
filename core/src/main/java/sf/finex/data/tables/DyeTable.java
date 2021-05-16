/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data.tables;

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
import sf.finex.StorageTable;
import sf.finex.data.DyeData;

/**
 *
 * @author FinFan
 */
@Slf4j
public final class DyeTable extends StorageTable {

	@Getter
	private static final DyeTable instance = new DyeTable();

	private final List<DyeData> holder = new ArrayList<>();

	public DyeTable() {
		load();
	}

	@Override
	public void reload() {
		holder.clear();
		load();
		log.info("Dye table reloaded!");
	}

	@Override
	protected void load() {
		final Gson gson = new Gson();
		final File file = new File("data/json/dyes.json");
		try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
			holder.addAll(gson.fromJson(reader, new TypeToken<List<DyeData>>() {
			}.getType()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		log.info("Loaded: {} tatoos templates.", holder.size());
	}

	@Override
	public DyeData get(int identifier) {
		for (DyeData symbol : holder) {
			if (symbol.getSymbolId() == identifier) {
				return symbol;
			}
		}

		return null;
	}

	@Override
	public List<DyeData> holder() {
		return holder;
	}
}
