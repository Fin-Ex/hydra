/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.item.kind;

import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.sf.l2j.gameserver.model.item.type.ArmorType;
import net.sf.l2j.gameserver.model.item.type.JewelType;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 *
 * @author FinFan
 */
public class Jewel extends Item {

	@Getter
	private final JewelType type;

	public Jewel(StatsSet set) {
		super(set);
		final int part = getBodyPart();
		if (part == Item.SLOT_NECK || part == Item.SLOT_FACE || part == Item.SLOT_HAIR || part == Item.SLOT_HAIRALL || (part & Item.SLOT_L_EAR) != 0 || (part & Item.SLOT_L_FINGER) != 0 || (part & Item.SLOT_BACK) != 0) {
			_type1 = Item.TYPE1_WEAPON_RING_EARRING_NECKLACE;
			_type2 = Item.TYPE2_ACCESSORY;
		}
		type = set.getEnum("jewel_type", JewelType.class, JewelType.NORMAL);
	}

	@Override
	public ArmorType getItemType() {
		return ArmorType.NONE;
	}

	@Override
	public int getItemMask() {
		return getItemType().mask();
	}

}
