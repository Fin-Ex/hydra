/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items.enums;

import lombok.Getter;
import net.sf.finex.IEnum;

/**
 *
 * @author finfan
 */
public enum ESoulCrystal implements IEnum {
	BLUE(new int[]{4651, 4652, 4653, 4654, 4655, 4656, 4657, 4658, 4659, 4660, 4661}),
	RED(new int[]{4629, 4630, 4631, 4632, 4633, 4634, 4635, 4636, 4637, 4638, 4639}),
	GREEN(new int[]{4640, 4641, 4642, 4643, 4644, 4645, 4646, 4647, 4648, 4649, 4650});

	@Getter private final int[] itemIds;

	private ESoulCrystal(int[] itemId) {
		this.itemIds = itemId;
	}

	public final int getItemId(int SAlevel) {
		return itemIds[SAlevel - 1];
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

	@Override
	public int getMask() {
		return 1 << ordinal();
	}
}
