/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items.enums;

import lombok.Getter;
import net.sf.finex.IEnum;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.ChanceCondition;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public enum ESpecialAbility implements IEnum {
	ACUMEN(3047) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	ANGER(3012) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	BACK_BLOW(3018) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	CHEAP_SHOT {					// Greatly reduces the amount of MP used when attacking with a bow, for every shot.
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	CONVERSION(3048) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	CRT_ANGER(3026) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_CRIT;
		}

		@Override
		public int getTriggerChance() {
			return 100;
		}
	},
	CRT_BLEED(3026) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_CRIT;
		}

		@Override
		public int getTriggerChance() {
			return 100;
		}
	},
	CRT_DAMAGE {				// When crit, gives extra damage. For different weapon - different numbers
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},					
	CRT_DRAIN(3022) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_CRIT;
		}

		@Override
		public int getTriggerChance() {
			return 100;
		}
	},
	CRT_POISON(3024) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_CRIT;
		}

		@Override
		public int getTriggerChance() {
			return 100;
		}
	},
	CRT_STUN(3016) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_CRIT;
		}

		@Override
		public int getTriggerChance() {
			return 100;
		}
	},
	EMPOWER(3072) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	EVASION(3009) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	FOCUS(3010) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	GUIDANCE(3007) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	HASTE(3036) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	HEALTH(3013) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	LIGHT {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},						// Lowers the weapon's weight by 70%.
	LONG_BLOW {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},					// Polearm range increases permanently.
	MAGIC_BLESS_BODY(1045) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_CHAOS(1222) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_DAMAGE {				// When using harmful magic on a target, it delivers additional magic damage with a fixed percentage.
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	MAGIC_FOCUS(1077) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_HOLD(1201) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 10;
		}
	},
	MAGIC_MENTAL_SHIELD(1035) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_MIGHT(1068) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_PARALYZE(3075) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 10;
		}
	},
	MAGIC_POISON(1168) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 10;
		}
	},
	MAGIC_POWER(3073) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 10;
		}
	},
	MAGIC_REGENERATION(1044) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_SHIELD(1040) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MAGIC_SILENCE(1064) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 10;
		}
	},
	MAGIC_WEAKNESS(1164) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_OFFENSIVE;
		}

		@Override
		public int getTriggerChance() {
			return 10;
		}
	},
	MANA_UP(3014) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	MENTAL_SHIELD(1035) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return ChanceCondition.TriggerType.ON_MAGIC_GOOD;
		}

		@Override
		public int getTriggerChance() {
			return 20;
		}
	},
	MIGHT_MORTAL(3035) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	MISER {							// Chance of shoulshot consumption decreasing by 0 to 4 shots per attack.
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	QUICK_RECOVERY {				// Reuse/Cast delay decreases permanently.
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	RSK_EVASION(3028) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	RSK_FOCUS(3027) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	RSK_HASTE(3032) {
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	},
	TOWERING_BLOW {					// Polearm effective angle increases permanently.
		@Override
		public ChanceCondition.TriggerType getTriggerType() {
			return null;
		}

		@Override
		public int getTriggerChance() {
			return -1;
		}
	};

	@Getter private final int skillId;

	private ESpecialAbility(int skillId) {
		this.skillId = skillId;
	}

	private ESpecialAbility() {
		this.skillId = 0;
	}

	public L2Skill getSkill(int casterLevel) {
		if(skillId == 0) {
			throw new UnsupportedOperationException("SA " + getNormalName() + " doesnt have a skillId.");
		}
		
		return SkillTable.getInstance().getLevelFrom(skillId, casterLevel);
	}
	
	@Override
	public int getId() {
		return ordinal();
	}

	@Override
	public String getEnumName() {
		return name();
	}

	@Override
	public String getNormalName() {
		String name = name().substring(0, 1); // upper case 1 symbol
		name += name().substring(1, name().length()).toLowerCase().replace("_", " "); // other symbols is lower case
		return name;
	}

	@Override
	public int getMask() {
		return 1 << ordinal();
	}
	
	public abstract ChanceCondition.TriggerType getTriggerType();
	
	public abstract int getTriggerChance();
}
