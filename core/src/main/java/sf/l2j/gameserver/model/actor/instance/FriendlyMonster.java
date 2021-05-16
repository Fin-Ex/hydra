package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.Attackable;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 * This class represents Friendly Mobs lying over the world.<br>
 * These friendly mobs should only attack players with karma > 0 and it is
 * always aggro, since it just attacks players with karma.
 */
public class FriendlyMonster extends Attackable {

	public FriendlyMonster(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public boolean isAutoAttackable(Creature attacker) {
		return attacker instanceof Player && ((Player) attacker).getKarma() > 0;
	}

	@Override
	public boolean isAggressive() {
		return true;
	}
}
