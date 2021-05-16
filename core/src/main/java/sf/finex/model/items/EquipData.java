/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.items;

import java.util.List;
import lombok.Data;
import sf.finex.model.items.enums.EItemSlot;
import sf.finex.model.items.parts.PartDataArmor;
import sf.finex.model.items.parts.PartDataJewel;
import sf.finex.model.items.parts.PartDataWeapon;
import sf.l2j.gameserver.model.item.type.EtcItemType;
import sf.l2j.gameserver.model.item.type.ItemType;
import sf.l2j.gameserver.skills.basefuncs.FuncTemplate;

/**
 *
 * @author finfan
 */
@Data
public class EquipData extends ItemData {

	private EItemSlot slot;
	private PartDataWeapon weapon;
	private PartDataArmor armor;
	private PartDataJewel jewel;
	private List<FuncTemplate> funcs; // stats which gives when ITEM just stay in inventory

	@Override
	public ItemType getType() {
		if(isWeapon()) {
			return weapon.getType();
		}
		
		if(isJewel()) {
			return EtcItemType.NONE;
		}
		
		if(isArmor()) {
			return armor.getType();
		}
		
		throw new UnsupportedOperationException("Not found item type for " + getId());
	}
	
	@Override
	public boolean isWeapon() {
		return weapon != null;
	}
	
	@Override
	public boolean isArmor() {
		return armor != null;
	}
	
	@Override
	public boolean isJewel() {
		return jewel != null;
	}

	@Override
	public boolean isEquipable() {
		return true;
	}
	
	
}
