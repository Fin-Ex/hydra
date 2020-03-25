/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents.effects;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author FinFan
 */
@Effect("Disarm")
public class Disarm extends L2Effect {

	public Disarm(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.DISARM;
	}

	@Override
	public boolean onStart() {
		if (getEffected().isPlayer()) {
			disarmRightHand();

			// hidden funcs
			final int leftHandDropRate = (int) (Math.sqrt(getEffector().getSTR()) * Math.sqrt(getEffector().getDEX() / 2) / 100);
			if (leftHandDropRate > Rnd.get(100)) {
				disarmLeftHand();
			}
		} else {
			getEffected().setAttackingDisabled(true);
		}
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public void onExit() {
		getEffected().setAttackingDisabled(false);
		super.onExit();
	}

	private void disarmRightHand() {
		final Player target = getEffected().getPlayer();
		if (target.isCursedWeaponEquipped()) {
			return;
		}

		// Unequip the weapon
		ItemInstance rightHandWeapon = target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if (rightHandWeapon != null) {
			if (target.isCastingNow()) {
				target.abortAttack();
			}
			target.abortCast();
			ItemInstance[] unequipped = target.getInventory().unEquipItemInBodySlotAndRecord(rightHandWeapon);
			InventoryUpdate iu = new InventoryUpdate();
			for (ItemInstance itm : unequipped) {
				iu.addModifiedItem(itm);
			}
			target.sendPacket(iu);
			target.abortAttack();
			target.broadcastUserInfo();

			// this can be 0 if the user pressed the right mousebutton twice very fast
			if (unequipped.length > 0) {
				SystemMessage sm;
				if (unequipped[0].getEnchantLevel() > 0) {
					sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED).addNumber(unequipped[0].getEnchantLevel()).addItemName(unequipped[0]);
				} else {
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED).addItemName(unequipped[0]);
				}

				target.sendPacket(sm);

				if (getEffector().getFirstEffect(L2EffectType.PARRY) != null) {
					// block equip right hand
					SkillTable.getInstance().getInfo(5300, 1).getEffects(getEffector(), getEffector());
				}
			}
		}
	}

	/**
	 * Hidden function.
	 */
	private void disarmLeftHand() {
		final Player target = getEffected().getPlayer();
		if (target.isCursedWeaponEquipped()) {
			return;
		}

		// Unequip the shield
		ItemInstance leftHandWeapon = target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		//getskill id -> apply to target for deny equip specified slot
		if (leftHandWeapon != null) {
			ItemInstance[] unequipped = target.getInventory().unEquipItemInBodySlotAndRecord(leftHandWeapon);
			InventoryUpdate iu = new InventoryUpdate();
			for (ItemInstance itm : unequipped) {
				iu.addModifiedItem(itm);
			}

			target.sendPacket(iu);
			target.broadcastUserInfo();

			// this can be 0 if the user pressed the right mousebutton twice very fast
			if (unequipped.length > 0) {
				SystemMessage sm;
				if (unequipped[0].getEnchantLevel() > 0) {
					sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED).addNumber(unequipped[0].getEnchantLevel()).addItemName(unequipped[0]);
				} else {
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED).addItemName(unequipped[0]);
				}

				target.sendPacket(sm);

				if (getEffector().getFirstEffect(L2EffectType.PARRY) != null) {
					// block equip left hand
					SkillTable.getInstance().getInfo(5300, 2).getEffects(getEffector(), getEffector());
				}
			}
		}
	}
}
