package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.finex.enums.EDependType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("DamOverTime")
public class EffectDamOverTime extends L2Effect {

	protected final EDependType depend;
	protected final double dependValue;
	protected double damage;

	public EffectDamOverTime(Env env, EffectTemplate template) {
		super(env, template);
		depend = template.stats.getEnum("dependType", EDependType.class, EDependType.None);
		dependValue = template.stats.getDouble("dependValue", 1.0);
		damage = calc();
		switch (depend) {
			case PhysicalDamage: {
				damage *= getEffector().getPAtk(null) * dependValue;
				break;
			}
			case MagicalDamage: {
				damage *= getEffector().getMAtk(null, null) * dependValue;
				break;
			}
		}
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.DMG_OVER_TIME;
	}

	@Override
	public boolean onActionTime() {
		if (getEffected().isDead()) {
			return false;
		}

		double damage = calc();
		if (damage >= getEffected().getCurrentHp()) {
			if (getSkill().isToggle()) {
				getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_HP));
				return false;
			}

			// For DOT skills that will not kill effected player.
			if (!getSkill().killByDOT()) {
				// Fix for players dying by DOTs if HP < 1 since reduceCurrentHP method will kill them
				if (getEffected().getCurrentHp() <= 1) {
					return true;
				}

				damage = getEffected().getCurrentHp() - 1;
			}
		}
		getEffected().reduceCurrentHpByDOT(damage, getEffector(), getSkill());

		return true;
	}
}
