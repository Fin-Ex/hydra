package net.sf.l2j.gameserver.handler.itemhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * That handler is used for the different types of keys. Such items aren't
 * consumed until the skill is definitively launched.
 *
 * @author Tryskell
 */
public class Keys implements IItemHandler {

	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
		if (!(playable instanceof Player)) {
			return;
		}

		final Player activeChar = (Player) playable;
		if (activeChar.isSitting()) {
			activeChar.sendPacket(SystemMessageId.CANT_MOVE_SITTING);
			return;
		}

		if (activeChar.isMovementDisabled()) {
			return;
		}

		final Creature target = (Creature) activeChar.getTarget();

		// Target must be a valid chest (not dead or already interacted).
		if (!(target instanceof Chest) || target.isDead() || ((Chest) target).isInteracted()) {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		if (item.getItem().hasStaticSkills()) {
			final L2Skill itemSkill = item.getItem().getStaticSkills().get(0).getSkill();
			if (itemSkill == null) {
				return;
			}

			// Key consumption is made on skill call, not on item call.
			playable.useMagic(itemSkill, false, false);
		}
	}
}
