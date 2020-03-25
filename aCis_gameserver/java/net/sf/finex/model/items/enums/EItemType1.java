/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items.enums;

import net.sf.finex.IEnum;

/**
 *
 * @author finfan
 */
public enum EItemType1 implements IEnum {

	WEAPON_RING_EARRING_NECKLACE,
	SHIELD_ARMOR,
	UNK1,
	UNK2,
	ITEM_QUESTITEM_ADENA;

	@Override
	public int getMask() {
		return 1 << ordinal();
	}

	@Override
	public int getId() {
		return ordinal();
	}

	@Override
	public String getEnumName() {
		return name();
	}

	@Override
	public String getNormalName() {
		return name().replace("_", " ");
	}
}
