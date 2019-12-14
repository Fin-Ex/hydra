package net.sf.l2j.gameserver.model.actor.events;

import org.slf4j.LoggerFactory;

import lombok.Data;
import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.l2j.gameserver.model.actor.Creature;

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
