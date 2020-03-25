package net.sf.l2j.gameserver.skills.basefuncs;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;

public class FuncEnchant extends Func {

	public FuncEnchant(Stats pStat, int pOrder, Object owner, Lambda lambda) {
		super(pStat, pOrder, owner, lambda);
	}

	@Override
	public void calc(Env env) {
		if (cond != null && !cond.test(env)) {
			return;
		}

		final ItemInstance item = (ItemInstance) funcOwner;

		int enchant = item.getEnchantLevel();
		if (enchant <= 0) {
			return;
		}

		int overenchant = 0;

		if (enchant > 3) {
			overenchant = enchant - 3;
			enchant = 3;
		}

		if (stat == Stats.MDef || stat == Stats.PDef) {
			env.addValue(enchant + 3 * overenchant);
			return;
		}

		if (stat == Stats.MAtk) {
			switch (item.getItem().getCrystalType()) {
				case S:
					env.addValue(4 * enchant + 8 * overenchant);
					break;

				case A:
				case B:
				case C:
					env.addValue(3 * enchant + 6 * overenchant);
					break;

				case D:
					env.addValue(2 * enchant + 4 * overenchant);
					break;
			}
			return;
		}

		if (item.isWeapon()) {
			final WeaponType type = (WeaponType) item.getItemType();

			switch (item.getItem().getCrystalType()) {
				case S:
					switch (type) {
						case BOW:
							env.addValue(10 * enchant + 20 * overenchant);
							break;

						case BIGBLUNT:
						case BIGSWORD:
						case DUALFIST:
						case DUAL:
							env.addValue(6 * enchant + 12 * overenchant);
							break;

						default:
							env.addValue(5 * enchant + 10 * overenchant);
							break;
					}
					break;

				case A:
					switch (type) {
						case BOW:
							env.addValue(8 * enchant + 16 * overenchant);
							break;

						case BIGBLUNT:
						case BIGSWORD:
						case DUALFIST:
						case DUAL:
							env.addValue(5 * enchant + 10 * overenchant);
							break;

						default:
							env.addValue(4 * enchant + 8 * overenchant);
							break;
					}
					break;

				case B:
					switch (type) {
						case BOW:
							env.addValue(6 * enchant + 12 * overenchant);
							break;

						case BIGBLUNT:
						case BIGSWORD:
						case DUALFIST:
						case DUAL:
							env.addValue(4 * enchant + 8 * overenchant);
							break;

						default:
							env.addValue(3 * enchant + 6 * overenchant);
							break;
					}
					break;

				case C:
					switch (type) {
						case BOW:
							env.addValue(6 * enchant + 12 * overenchant);
							break;

						case BIGBLUNT:
						case BIGSWORD:
						case DUALFIST:
						case DUAL:
							env.addValue(4 * enchant + 8 * overenchant);
							break;

						default:
							env.addValue(3 * enchant + 6 * overenchant);
							break;
					}
					break;

				case D:
					switch (type) {
						case BOW:
							env.addValue(4 * enchant + 8 * overenchant);
							break;

						default:
							env.addValue(2 * enchant + 4 * overenchant);
							break;
					}
					break;
			}
		}
	}
}
