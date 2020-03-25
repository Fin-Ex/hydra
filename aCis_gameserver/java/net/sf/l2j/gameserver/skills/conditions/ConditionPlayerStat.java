package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * @author mr
 */
public class ConditionPlayerStat extends Condition {

	private Stats stat;
	private int value;
	private boolean isLower;

	public ConditionPlayerStat(Stats stat, int value, boolean isLower) {
		this.stat = stat;
		this.value = value;
		this.isLower = isLower;
	}

	@Override
	public boolean testImpl(Env env) {
		final Creature cha = env.getCharacter();
		switch (stat) {
			case STR:
				return isLower ? cha.getSTR() < value : cha.getSTR() >= value;
			case DEX:
				return isLower ? cha.getDEX() < value : cha.getDEX() >= value;
			case CON:
				return isLower ? cha.getCON() < value : cha.getCON() >= value;
			case INT:
				return isLower ? cha.getINT() < value : cha.getINT() >= value;
			case MEN:
				return isLower ? cha.getMEN() < value : cha.getMEN() >= value;
			case WIT:
				return isLower ? cha.getWIT() < value : cha.getWIT() >= value;
		}
		return false;
	}
}
