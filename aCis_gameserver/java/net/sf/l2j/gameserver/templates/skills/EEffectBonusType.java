/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.templates.skills;

import lombok.Getter;
import net.sf.l2j.gameserver.skills.effects.bonus.BonusRhythm;
import net.sf.l2j.gameserver.skills.effects.bonus.BonusSingingWeapon;
import net.sf.l2j.gameserver.skills.effects.bonus.IBonusHandler;

/**
 *
 * @author FinFan
 */
public enum EEffectBonusType {
	NONE,
	RHYTHM(new BonusRhythm()),
	SING_SWORD(new BonusSingingWeapon());
	
	@Getter private final IBonusHandler handler;

	private EEffectBonusType(IBonusHandler handler) {
		this.handler = handler;
	}

	private EEffectBonusType() {
		this.handler = null;
	}
}
