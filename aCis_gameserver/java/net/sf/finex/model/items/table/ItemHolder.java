/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items.table;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.DataParser;
import net.sf.finex.model.items.EquipData;
import net.sf.finex.model.items.ItemData;
import net.sf.finex.model.items.QuestData;
import net.sf.finex.model.items.UsableData;

/**
 *
 * @author finfan
 */
@Slf4j
public class ItemHolder {

	@Getter private static final ItemHolder instance = new ItemHolder();

	@Getter private final List<ItemData> holder;

	@Getter private final List<EquipData> armor;
	@Getter private final List<EquipData> weapon;
	@Getter private final List<EquipData> jewel;
	@Getter private final List<QuestData> quest;
	@Getter private final List<UsableData> usable;
	@Getter private final List<ItemData> etc;

	private ItemHolder() {
		holder = DataParser.getInstance().parseAndGet("data/json/items/", ItemData[].class);

		armor = new ArrayList<>();
		weapon = new ArrayList<>();
		jewel = new ArrayList<>();
		quest = new ArrayList<>();
		usable = new ArrayList<>();
		etc = new ArrayList<>();
		for (ItemData nextItem : holder) {
			if (nextItem.isArmor()) {
				armor.add((EquipData) nextItem);
			} else if (nextItem.isWeapon()) {
				weapon.add((EquipData) nextItem);
			} else if (nextItem.isJewel()) {
				jewel.add((EquipData) nextItem);
			} else if (nextItem.isQuest()) {
				quest.add((QuestData) nextItem);
			} else if (nextItem.isUsable()) {
				usable.add((UsableData) nextItem);
			} else if (nextItem.isEtc()) {
				etc.add(nextItem);
			}
		}
		log.info("Loaded: {} item templates.", holder.size());
		log.info("Hightest used item id = {}", holder.get(holder.size() - 1).getId());
	}
}
