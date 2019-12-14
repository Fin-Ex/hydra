/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;
import net.sf.l2j.gameserver.model.item.kind.Jewel;
import net.sf.l2j.gameserver.model.item.kind.Weapon;

/**
 *
 * @author FinFan
 */
public enum ECraftSpec {
	ARMOR("Armor Specialize", "Armor"),
	WEAPON("Weapon Specialize", "Weapon"),
	JEWEL("Jewel Specialize", "Jewel"),
	PRODUCTION("Production Specialize", "Production");

	@Getter
	private final String name;
	@Getter
	private final String simpleName;

	private ECraftSpec(String name, String simpleName) {
		this.name = name;
		this.simpleName = simpleName;
	}

	public boolean checkItem(Class<?> itemType) {
		switch (this) {
			case ARMOR:
				return itemType == Armor.class;

			case WEAPON:
				return itemType == Weapon.class;

			case JEWEL:
				return itemType == Jewel.class;

			default:
				return itemType == EtcItem.class;
		}
	}
}
