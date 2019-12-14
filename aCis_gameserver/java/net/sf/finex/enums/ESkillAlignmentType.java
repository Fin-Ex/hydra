/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import lombok.Getter;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;

/**
 *
 * @author FinFan
 */
public enum ESkillAlignmentType {
	/**
	 * Dependancy: Physical mute
	 */
	PHYSIC(EEffectFlag.PHYSICAL_MUTED),
	/**
	 * Dependancy: Magical mute
	 */
	MAGIC(EEffectFlag.MAGIC_MUTED),
	/**
	 * Dependancy: Cant be muted by silence skills
	 */
	POTION(EEffectFlag.POTION_MUTED),
	/**
	 * Can't be silenced Race skills
	 */
	RACE(EEffectFlag.RACE_MUTED),
	/**
	 * Profession (Mining, Skinning, Tailoring and etc..) skills
	 *
	 * @Deprecated
	 */
	PROFESSION(EEffectFlag.PROFESSION_MUTED),
	/**
	 * Ultimate skills cant be silenced
	 */
	ULTIMATE(EEffectFlag.ULTIAMTE_MUTED),
	/**
	 * Abilitiy skills is like toggled which can worn on or turned off
	 */
	ABILITY(EEffectFlag.ABILITY_MUTED);

	public static final ESkillAlignmentType[] VALUES = values();
	@Getter
	private final EEffectFlag effectFlag;

	private ESkillAlignmentType(EEffectFlag effectFlag) {
		this.effectFlag = effectFlag;
	}

	public String getName() {
		return name().substring(0, 1) + name().substring(1).toLowerCase();
	}
}
