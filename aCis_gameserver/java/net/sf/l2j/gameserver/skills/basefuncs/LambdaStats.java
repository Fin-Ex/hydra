package net.sf.l2j.gameserver.skills.basefuncs;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public final class LambdaStats extends Lambda {

	public enum StatsType {
		PLAYER_LEVEL,
		TARGET_LEVEL,
		PLAYER_MAX_HP,
		PLAYER_MAX_MP,
		SA_CRT_ANGER_CATKADD,
		SA_CRT_ANGER_HPREDUCE,
		SA_EMPOWER,
		SA_EVASION,
		SA_FOCUS,
		SA_GUIDANCE,
		SA_MAGIC_POWER,
		SA_RSK_FOCUS,
		SA_RSK_HASTE,
		SA_RSK_EVASION,
		TLNT_5_ATK,
		TLNT_5_ASPD,
		TLNT_5_ACC;

		@Override
		public String toString() {
			return "$" + name();
		}
	}

	private final StatsType _stat;

	public LambdaStats(StatsType stat) {
		_stat = stat;
	}

	@Override
	public double calc(Env env) {
		if (env.getCharacter() == null) {
			return 1.;
		}

		if (_stat == null) {
			return 1;
		}

		final Creature cha = env.getCharacter();
		switch (_stat) {
			case PLAYER_LEVEL:
				return cha.getLevel();

			case TARGET_LEVEL:
				return cha.getLevel();

			case PLAYER_MAX_HP:
				return cha.getMaxHp();

			case PLAYER_MAX_MP:
				return cha.getMaxMp();

			case SA_CRT_ANGER_CATKADD:
				return 223 + Math.pow(cha.getLevel() / 3., 2);

			case SA_CRT_ANGER_HPREDUCE:
				return Math.pow(cha.getLevel() / 12., 2);

			case SA_EMPOWER:
				return cha.getLevel() * 2.25;

			case SA_EVASION:
			case SA_GUIDANCE:
				return Math.pow(cha.getLevel() / 15., 2);

			case SA_FOCUS:
				return 56 + Math.pow(cha.getLevel() / 8., 2);

			case SA_MAGIC_POWER:
				return Rnd.get(100, 200) / 100. + 1;

			case SA_RSK_FOCUS:
				return 1000 - (1000 * calcPercent(cha, 60));

			case SA_RSK_HASTE:
				return 1500 - (1500 * calcPercent(cha, 60));

			case SA_RSK_EVASION:
				return 100 - (100 * calcPercent(cha, 60));

			default:
				// confrontation handling
				if (_stat.name().startsWith("TLNT_5")) {
					double aspd = 1.0;
					double atk = 1.0;
					int acc = 0;
					for (Creature creatures : cha.getKnownTypeInRadius(Creature.class, 350)) {
						if (creatures.isAutoAttackable(cha) && creatures.getTarget() == cha) {
							aspd += 0.05;
							atk += 0.05;
							acc += 2;
						}
					}

					switch (_stat) {
						case TLNT_5_ASPD:
							return aspd;
						case TLNT_5_ACC:
							return acc;
						case TLNT_5_ATK:
							return atk;
					}

					// send status update for params
					if (aspd > 1 || atk > 1 || acc > 0) {
						final StatusUpdate su = new StatusUpdate(cha);
						su.addAttribute(StatusUpdate.ATK_SPD, cha.getPAtkSpd());
						su.addAttribute(StatusUpdate.ACCURACY, cha.getAccuracy());
						su.addAttribute(StatusUpdate.P_ATK, cha.getPAtk(cha));
						cha.sendPacket(su);
					}

				}
		}
		return 0;
	}

	private double calcPercent(Creature cha, int value) {
		double incValue = cha.getCurrentHp() / cha.getMaxHp() * 100. / value;
		if (incValue * 100 > value) {
			return 0.0;
		}
		return incValue;
	}
}
