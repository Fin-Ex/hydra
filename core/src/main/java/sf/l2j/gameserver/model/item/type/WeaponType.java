package sf.l2j.gameserver.model.item.type;

import lombok.Getter;
import sf.finex.model.creature.attack.AbstractHit;
import sf.finex.model.creature.attack.Bow;
import sf.finex.model.creature.attack.Dual;
import sf.finex.model.creature.attack.Mass;
import sf.finex.model.creature.attack.Simple;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.skills.Stats;

/**
 * @author mkizub
 */
public enum WeaponType implements ItemType {
	NONE(40, null),
	SWORD(40, Stats.SwordWpnVuln),
	BLUNT(40, Stats.BluntWpnVuln),
	DAGGER(40, Stats.DaggerWpnVuln),
	BOW(500, Stats.BowWpnVuln),
	POLE(66, Stats.PoleWpnVuln),
	ETC(40, null),
	FIST(40, Stats.DualFistWpnVuln),
	DUAL(40, Stats.DualWpnVuln),
	DUALFIST(40, Stats.DualFistWpnVuln),
	BIGSWORD(40, Stats.BigSwordWpnVuln),
	FISHINGROD(40, null),
	BIGBLUNT(40, Stats.BigBluntWpnVuln),
	PET(40, null);

	private final int mask;
	private final int range;
	@Getter
	private final Stats vuln;

	private WeaponType(int range, Stats vuln) {
		this.mask = 1 << ordinal();
		this.range = range;
		this.vuln = vuln;
	}

	/**
	 * Returns the ID of the item after applying the mask.
	 *
	 * @return int : ID of the item
	 */
	@Override
	public int mask() {
		return mask;
	}

	public int getRange() {
		return range;
	}

	public final AbstractHit createHit(Creature attacker, Creature target) {
		switch (this) {
			case DUAL:
			case DUALFIST:
			case FIST:
				return new Dual(attacker, target);

			case BOW:
				return new Bow(attacker, target);

			case POLE:
				return new Mass(attacker, target);

			default:
				return new Simple(attacker, target);
		}
	}
}
