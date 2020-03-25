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
public enum EItemSlot implements IEnum {
	
	SLOT_NONE(0x0000),
	SLOT_UNDERWEAR(0x0001),
	SLOT_R_EAR(0x0002),
	SLOT_L_EAR(0x0004),
	SLOT_LR_EAR(0x00006),
	SLOT_NECK(0x0008),
	SLOT_R_FINGER(0x0010),
	SLOT_L_FINGER(0x0020),
	SLOT_LR_FINGER(0x0030),
	SLOT_HEAD(0x0040),
	SLOT_R_HAND(0x0080),
	SLOT_L_HAND(0x0100),
	SLOT_GLOVES(0x0200),
	SLOT_CHEST(0x0400),
	SLOT_LEGS(0x0800),
	SLOT_FEET(0x1000),
	SLOT_BACK(0x2000),
	SLOT_LR_HAND(0x4000),
	SLOT_FULL_ARMOR(0x8000),
	SLOT_FACE(0x010000),
	SLOT_ALLDRESS(0x020000),
	SLOT_HAIR(0x040000),
	SLOT_HAIRALL(0x080000),
	SLOT_WOLF(-100),
	SLOT_HATCHLING(-101),
	SLOT_STRIDER(-102),
	SLOT_BABYPET(-103),
	SLOT_ALLWEAPON(0x4000 | 0x0080);

	private final int id;

	private EItemSlot(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getEnumName() {
		return name();
	}

	@Override
	public String getNormalName() {
		String prefix = name().substring(5);
		String name = prefix.substring(0, 1);
		name += prefix.substring(6, prefix.length()).toLowerCase();
		return name;
	}
	
	public static EItemSlot getSlot(int id) {
		for(EItemSlot next : values()) {
			if(next.getId() == id) {
				return next;
			}
		}
		
		throw new NullPointerException("Slot with ID " + id + " not found.");
	}

	@Override
	public int getMask() {
		return 1 << ordinal();
	}
}
