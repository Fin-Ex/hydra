package net.sf.l2j.gameserver.model.actor.events;

import lombok.Data;
import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.l2j.gameserver.model.actor.Creature;

/**
 * OnBowHit
 *
 * @author zcxv
 * @date 19.05.2019
 */
@Data
public class OnBowHit {

	private final Creature attacker;
	private final Creature target;
	private final DamageInfo damageInfo;
}
