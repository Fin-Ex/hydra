/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents.effects;

import lombok.Getter;
import net.sf.finex.events.AbstractEventSubscription;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnUnequipItem;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.finex.enums.EGradeType;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.basefuncs.FuncSet;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author FinFan
 */
@Effect("HeavyGrip")
public class HeavyGrip extends L2Effect {
	
	@Getter private AbstractEventSubscription<OnUnequipItem> onUnequip;
	@Getter private int weaponObjectId;
	@Getter private int weaponItemId;
	
	public HeavyGrip(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.HEAVY_GRIP;
	}

	@Override
	public boolean onStart() {
		final Player player = getEffector().getPlayer();
		if(player == null) {
			return false;
		}
		
		switch(player.getAttackType()) {
			case DUAL:
				final Weapon weapon = player.getActiveWeaponItem();
				switch(weapon.getCrystalType()) {
					case D:
						// to Two-Hand Sword
						weaponItemId = 9209;
						break;
						
					case C:
						// to Flambergs
						weaponItemId = 9210;
						break;
						
					case B:
						// to Great swords
						weaponItemId = 9211;
						break;
						
					case A:
						// to Dragon Slayers
						weaponItemId = 9212;
						break;
						
					case S:
						// to Heavens dividers
						weaponItemId = 9213;
						break;
				}
				changeStats(player, weapon.getCrystalType());
				weaponObjectId = 0x01;
				break;
				
			default:
				return false;
		}
		
		onUnequip = getEffector().getEventBus().subscribe()
				.cast(OnUnequipItem.class)
				.forEach(this::onUnequip);
		
		player.broadcastUserInfo();
		return true;
	}

	private void onUnequip(OnUnequipItem e) {
		exit();
	}
	
	@Override
	public boolean onActionTime() {
		return true;
	}

	@Override
	public void onExit() {
		if(getEffector().getActiveWeaponItem() != null) {
			weaponItemId = getEffector().getActiveWeaponItem().getItemId();
		}
		getEffector().getPlayer().broadcastUserInfo();
		getEffector().getEventBus().unsubscribe(onUnequip);
		super.onExit();
	}
	
	private void changeStats(Player player, EGradeType grade) {
		switch(grade) {
			case D:
				player.addStatFunc(new FuncSet(Stats.PAtk, 0x08, this, new LambdaConst(156)));
				break;
			case C:
				player.addStatFunc(new FuncSet(Stats.PAtk, 0x08, this, new LambdaConst(260)));
				break;
			case B:
				player.addStatFunc(new FuncSet(Stats.PAtk, 0x08, this, new LambdaConst(426)));
				break;
			case A:
				player.addStatFunc(new FuncSet(Stats.PAtk, 0x08, this, new LambdaConst(564)));
				break;
			case S:
				player.addStatFunc(new FuncSet(Stats.PAtk, 0x08, this, new LambdaConst(684)));
				break;
		}
	}
}
