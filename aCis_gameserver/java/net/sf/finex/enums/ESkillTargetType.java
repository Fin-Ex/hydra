/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import org.slf4j.LoggerFactory;

/**
 * Target types of skills : SELF, PARTY, CLAN, PET...
 */
public enum ESkillTargetType {
	TARGET_NONE,
	TARGET_SELF,
	TARGET_ONE,
	TARGET_PARTY,
	TARGET_ALLY,
	TARGET_CLAN,
	TARGET_PET,
	TARGET_AREA,
	TARGET_FRONT_AREA,
	TARGET_BEHIND_AREA,
	TARGET_AURA,
	TARGET_FRONT_AURA,
	TARGET_BEHIND_AURA,
	TARGET_CORPSE,
	TARGET_UNDEAD,
	TARGET_AURA_UNDEAD,
	TARGET_CORPSE_ALLY,
	TARGET_CORPSE_PLAYER,
	TARGET_CORPSE_PET,
	TARGET_AREA_CORPSE_MOB,
	TARGET_CORPSE_MOB,
	TARGET_UNLOCKABLE,
	TARGET_HOLY,
	TARGET_PARTY_MEMBER,
	TARGET_PARTY_OTHER,
	TARGET_SUMMON,
	TARGET_AREA_SUMMON,
	TARGET_ENEMY_SUMMON,
	TARGET_OWNER_PET,
	TARGET_GROUND;
}
