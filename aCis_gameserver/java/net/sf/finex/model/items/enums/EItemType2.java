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
public enum EItemType2 implements IEnum {

	WEAPON,
	SHIELD,
	ACCESSORY,
	QUEST,
	MONEY,
	OTHER;

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
		String name = name().substring(0, 1); // upper case 1 symbol
		name += name().substring(1, name().length()).toLowerCase(); // other symbols is lower case
		return name;
	}

}
