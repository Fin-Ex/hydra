/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.skills.effects.bonus;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.item.type.WeaponType;

/**
 *
 * @author FinFan
 */
public class BonusSingingWeapon implements IBonusHandler {

	@Override
	public double calc(Creature caster) {
		if(!caster.isPlayer() || !caster.getPlayer().getClassId().equalsOrChildOf(ClassId.Swordsinger)) {
			return 1;
		}
		
		return caster.getAttackType() == WeaponType.BIGSWORD ? 1.15 : 1;
	}
	
}
