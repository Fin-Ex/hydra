package sf.l2j.gameserver.model.actor.events;

import lombok.Data;
import sf.finex.model.creature.attack.DamageInfo;
import sf.l2j.gameserver.model.actor.Creature;

/**
 * OnMassHit
 *
 * @author zcxv
 * @date 19.05.2019
 */
@Data
public class OnMassHit {

	private final Creature attacker;
	private final Creature target;
	private final DamageInfo damageInfo;
}
