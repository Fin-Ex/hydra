/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import net.sf.finex.ImgString;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.model.items.enums.EItemRestriction;
import net.sf.finex.model.items.enums.EItemSlot;
import net.sf.finex.model.items.enums.EItemType1;
import net.sf.finex.model.items.enums.EItemType2;
import net.sf.finex.model.items.parts.PartDataArmor;
import net.sf.finex.model.items.parts.PartDataJewel;
import net.sf.finex.model.items.parts.PartDataWeapon;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Jewel;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.item.type.ItemType;
import net.sf.l2j.gameserver.model.item.type.MaterialType;

/**
 *
 * @author finfan
 */
@Data
public class ItemData {

	private int id;
	private String name;
	private int weight;
	private MaterialType material;
	private EGradeType grade;
	private int crystalCount;
	private ImgString icon;
	private EItemType1 type1;
	private EItemType2 type2;
	private EItemRestriction[] restrictions = new EItemRestriction[0];

	public ItemType getType() {
		return EtcItemType.NONE;
	}
	
	public boolean isEquipable() {
		return false;
	}
	
	public boolean isWeapon() {
		return false;
	}
	
	public boolean isArmor() {
		return false;
	}
	
	public boolean isJewel() {
		return false;
	}
	
	public boolean isEtc() {
		return true;
	}
	
	public boolean isUsable() {
		return false;
	}
	
	public boolean isQuest() {
		return false;
	}
	
	public static final ItemData create(Item item) {
		if(item == null) {
			throw new NullPointerException("Item argument is null");
		}
		
		ItemData data;
		if (item instanceof Weapon) {
			final Weapon wpn = (Weapon) item;
			final EquipData equip = new EquipData();
			data = equip;

			equip.setFuncs(wpn.getFuncs());
			equip.setSlot(EItemSlot.getSlot(wpn.getBodyPart()));
			PartDataWeapon part = new PartDataWeapon();
			part.setHPConsume(wpn.getHpConsume());
			part.setMPConsume(wpn.getMpConsume());
			part.setEnchantSkill(wpn.getEnchant4Holder());
			part.setMagical(wpn.isMagical());
			part.setRandomDamage(wpn.getRandomDamage());
			part.setReuseDelay(wpn.getReuseDelay());
			part.setSPSConsume(wpn.getSpiritShotCount());
			part.setSSConsume(wpn.getSoulShotCount());
			part.setType(wpn.getItemType());
			equip.setWeapon(part);
		} else if (item instanceof Armor) {
			final Armor arm = (Armor) item;
			final EquipData equip = new EquipData();
			data = equip;

			equip.setFuncs(arm.getFuncs());
			equip.setSlot(EItemSlot.getSlot(arm.getBodyPart()));
			PartDataArmor part = new PartDataArmor();
			part.setType(arm.getItemType());
			equip.setArmor(part);
		} else if (item instanceof Jewel) {
			final Jewel jew = (Jewel) item;
			final EquipData equip = new EquipData();
			data = equip;

			equip.setFuncs(jew.getFuncs());
			equip.setSlot(EItemSlot.getSlot(jew.getBodyPart()));
			PartDataJewel part = new PartDataJewel();
			part.setType(jew.getType());
			equip.setJewel(part);
		} else {
			EtcItem etc = (EtcItem) item;
			if (item.isQuestItem()) {
				final QuestData quest = new QuestData();
				quest.setType(EtcItemType.QUEST);
				quest.setQuestEvents(etc.getQuestEvents());
				data = quest;
			} else if (etc.getHandlerName() != null && !etc.getHandlerName().isEmpty()) {
				final UsableData usable = new UsableData();
				usable.setAction(etc.getDefaultAction());
				usable.setHandler(etc.getHandlerName());
				usable.setSkills(item.getStaticSkills());
				usable.setType(etc.getItemType());
				data = usable;
			} else {
				data = new ItemData();
			}
		}

		data.setCrystalCount(item.getCrystalCount());
		data.setGrade(item.getCrystalType());
		data.setIcon(new ImgString(32, 32, item.getIcon()));
		data.setId(item.getItemId());
		data.setMaterial(item.getMaterialType());
		data.setName(item.getName());
		data.setWeight(item.getWeight());
		data.setType1(EItemType1.values()[item.getType1()]);
		data.setType2(EItemType2.values()[item.getType2()]);
		List<EItemRestriction> restrictions = new ArrayList<>();
		if (data.getType2() == EItemType2.QUEST) {
			restrictions.add(EItemRestriction.UNDEPOSITABLE);
			restrictions.add(EItemRestriction.UNDESTROYABLE);
			restrictions.add(EItemRestriction.UNDROPABLE);
			restrictions.add(EItemRestriction.UNSELLABLE);
			restrictions.add(EItemRestriction.UNTRADABLE);
		} else {
			if (!item.isDestroyable()) {
				restrictions.add(EItemRestriction.UNDESTROYABLE);
			}
			if (!item.isDepositable()) {
				restrictions.add(EItemRestriction.UNDEPOSITABLE);
			}
			if (!item.isDropable()) {
				restrictions.add(EItemRestriction.UNDROPABLE);
			}
			if (!item.isSellable()) {
				restrictions.add(EItemRestriction.UNSELLABLE);
			}
			if (!item.isTradable()) {
				restrictions.add(EItemRestriction.UNTRADABLE);
			}
			if (item.isOlyRestrictedItem()) {
				restrictions.add(EItemRestriction.UNOLYMPABLE);
			}
			if (!item.isStackable()) {
				restrictions.add(EItemRestriction.UNSTACKABLE);
			}
		}

		data.setRestrictions(restrictions.toArray(new EItemRestriction[restrictions.size()]));
		return data;
	}
}
