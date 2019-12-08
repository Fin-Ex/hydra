/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.item.instance;

import lombok.Getter;

/**
 *
 * @author zcxv
 */
@Getter
public class InventoryFlag {
	
	private final int slot;
	private boolean canEquip = true;
	private boolean canUnequip = true;

	public InventoryFlag(int slot) {
		this.slot = slot;
	}
	
	public InventoryFlag setCanEquip(boolean canEquip) {
		this.canEquip = canEquip;
		return this;
	}
	
	public InventoryFlag setCanUnequip(boolean canUnequip) {
		this.canUnequip = canUnequip;
		return this;
	}
	
	protected boolean checkAccess(int slot, FlagPolicy policy) {
		if(this.slot != slot) {
			return true;
		}
		
		switch(policy) {
			case Equip:
				return canEquip;
			case Unequip:
				return canUnequip;
		}
		
		return true;
	}
	
}
