/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.item.kind;

import lombok.Getter;
import sf.l2j.gameserver.model.item.type.ArmorType;
import sf.l2j.gameserver.model.item.type.JewelType;
import sf.l2j.gameserver.templates.StatsSet;

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
		if (part == SLOT_NECK || part == SLOT_FACE || part == SLOT_HAIR || part == SLOT_HAIRALL || (part & SLOT_L_EAR) != 0 || (part & SLOT_L_FINGER) != 0 || (part &
			SLOT_BACK) != 0) {
			_type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE;
			_type2 = TYPE2_ACCESSORY;
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
