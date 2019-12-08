/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents.effects;

import net.sf.l2j.gameserver.model.item.instance.InventoryFlag;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author zcxv
 */
@Effect("BlockEquip")
public class BlockEquip extends L2Effect {
	
	private final InventoryFlag denyFlag;

	public BlockEquip(Env env, EffectTemplate template) {
		super(env, template);
		denyFlag = new InventoryFlag(template.stats.getInteger("slot")).setCanEquip(false);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BLOCK_EQUIP;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public boolean onStart() {
		getEffected().getInventory().getFlags().addFlag(denyFlag);
		return super.onStart();
	}

	@Override
	public void onExit() {
		getEffected().getInventory().getFlags().removeFlag(denyFlag);
	}
	
}
