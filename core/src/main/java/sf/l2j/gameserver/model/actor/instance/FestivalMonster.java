package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.instancemanager.SevenSignsFestival;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 * L2FestivalMonsterInstance This class manages all attackable festival NPCs,
 * spawned during the Festival of Darkness.
 *
 * @author Tempy
 */
public class FestivalMonster extends Monster {

	protected int _bonusMultiplier = 1;

	public FestivalMonster(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	public void setOfferingBonus(int bonusMultiplier) {
		_bonusMultiplier = bonusMultiplier;
	}

	/**
	 * Return True if the attacker is not another L2FestivalMonsterInstance.
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker) {
		if (attacker instanceof FestivalMonster) {
			return false;
		}

		return true;
	}

	/**
	 * All mobs in the festival are aggressive, and have high aggro range.
	 */
	@Override
	public boolean isAggressive() {
		return true;
	}

	/**
	 * All mobs in the festival don't need random animation.
	 */
	@Override
	public boolean hasRandomAnimation() {
		return false;
	}

	/**
	 * Add a blood offering item to the leader of the party.
	 */
	@Override
	public void doItemDrop(Creature attacker) {
		final Player player = attacker.getPlayer();
		if (player == null || !player.isInParty()) {
			return;
		}

		player.getParty().getLeader().addItem("Sign", SevenSignsFestival.FESTIVAL_OFFERING_ID, _bonusMultiplier, attacker, true);

		super.doItemDrop(attacker);
	}
}
