package net.sf.l2j.gameserver.handler.itemhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Pet;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ScrollOfResurrection implements IItemHandler {

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

		// Target must be a dead pet or player.
		if ((!(target instanceof Pet) && !(target instanceof Player)) || !target.isDead()) {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		// Pet scrolls to ress a player.
		if (item.getItemId() == 6387 && target instanceof Player) {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		// Pickup player, or pet owner in case target is a pet.
		final Player targetPlayer = target.getPlayer();

		// Check if target isn't in a active siege zone.
		final Siege siege = CastleManager.getInstance().getSiege(targetPlayer);
		if (siege != null) {
			activeChar.sendPacket(SystemMessageId.CANNOT_BE_RESURRECTED_DURING_SIEGE);
			return;
		}

		// Check if the target is in a festival.
		if (targetPlayer.isFestivalParticipant()) {
			return;
		}

		if (targetPlayer.isReviveRequested()) {
			if (targetPlayer.isRevivingPet()) {
				activeChar.sendPacket(SystemMessageId.MASTER_CANNOT_RES); // While a pet is attempting to resurrect, it cannot help in resurrecting its master.
			} else {
				activeChar.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED); // Resurrection is already been proposed.
			}
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
