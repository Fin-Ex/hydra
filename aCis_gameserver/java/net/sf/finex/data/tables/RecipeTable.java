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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.StorageTable;
import net.sf.finex.data.RecipeData;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Jewel;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.finex.enums.EGradeType;

/**
 *
 * @author FinFan
 */
@Slf4j
public final class RecipeTable extends StorageTable {

	@Getter private static final RecipeTable instance = new RecipeTable();

	public static int RECIPE_COUNT;

	private final List<RecipeData> holder = new ArrayList<>();
	private final Map<EGradeType, List<RecipeData>> weapons = new HashMap<>();
	private final Map<EGradeType, List<RecipeData>> armors = new HashMap<>();
	private final Map<EGradeType, List<RecipeData>> jewels = new HashMap<>();
	private final List<RecipeData> others = new ArrayList<>();
	
	public RecipeTable() {
		load();
	}

	public RecipeData get(int identifier, boolean byRecipeItemId) {
		if(!byRecipeItemId) {
			return get(identifier);
		}
		for(int i = 0; i < holder.size(); i++) {
			final RecipeData recipe = holder.get(i);
			final int id = recipe.getRecipeItemId();
			if(id == identifier) {
				return recipe;
			}
		}
		
		return null;
	}

	@Override
	public void load() {
		final Gson gson = new Gson();
		final File file = new File("data/json/craft/recipes.json");
		try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
			holder.addAll(gson.fromJson(reader, new TypeToken<List<RecipeData>>() {}.getType()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		log.info("Loaded: {} recipe templates.", holder.size());
		
		for(RecipeData data : holder) {
			final Item item = ItemTable.getInstance().getTemplate(data.getProduct().getId());
			final EGradeType grade = item.getCrystalType();
			switch(grade) {
				case B:
				case A:
				case S:
					if(data.getSuccessRate() == 100) {
						continue;
					}
					break;
			}
			
			if(item instanceof Weapon) {
				if(!weapons.containsKey(grade)) {
					weapons.put(grade, new ArrayList<>());
				}
				weapons.get(grade).add(data);
			} else if(item instanceof Armor) {
				if(!armors.containsKey(grade)) {
					armors.put(grade, new ArrayList<>());
				}
				armors.get(grade).add(data);
			} else if(item instanceof Jewel) {
				if(!jewels.containsKey(grade)) {
					jewels.put(grade, new ArrayList<>());
				}
				jewels.get(grade).add(data);
			} else {
				others.add(data);
			}
		}
		
		RECIPE_COUNT = holder.get(holder.size() - 1).getCraftId();
	}

	@Override
	public void reload() {
		holder.clear();
		load();
		log.info("Loaded: {} recipe templates. TODO: common recipes to true.", holder.size());
	}

	public List<RecipeData> getArmors(EGradeType grade) {
		return armors.get(grade);
	}

	public List<RecipeData> getJewels(EGradeType grade) {
		return jewels.get(grade);
	}

	public List<RecipeData> getOthers() {
		return others;
	}

	public List<RecipeData> getWeapons(EGradeType grade) {
		return weapons.get(grade);
	}

	@Override
	public RecipeData get(int identifier) {
		for(int i = 0; i < holder.size(); i++) {
			final RecipeData recipe = holder.get(i);
			if(recipe.getCraftId() == identifier) {
				return recipe;
			}
		}
		
		return null;
	}

	@Override
	public List<RecipeData> holder() {
		return holder;
	}
}
