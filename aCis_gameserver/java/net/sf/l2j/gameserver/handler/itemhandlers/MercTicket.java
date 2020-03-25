package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.SealType;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.item.MercenaryTicket;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Handler to use mercenary tickets.<br>
 * <br>
 * Check constraints:
 * <ul>
 * <li>Only specific tickets may be used in each castle (different tickets for
 * each castle)</li>
 * <li>Only the owner of that castle may use them</li>
 * <li>tickets cannot be used during siege</li>
 * <li>Check if max number of tickets from this ticket's TYPE has been
 * reached</li>
 * </ul>
 * If allowed, spawn the item in the world and remove it from the player's
 * inventory.
 */
public class MercTicket implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		final Player activeChar = (Player) playable;
		if (activeChar == null) {
			return;
		}

		final Castle castle = CastleManager.getInstance().getCastle(activeChar);
		if (castle == null) {
			return;
		}

		final int castleId = castle.getCastleId();

		// Castle lord check.
		if (!activeChar.isCastleLord(castleId)) {
			activeChar.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_AUTHORITY_TO_POSITION_MERCENARIES);
			return;
		}

		final int itemId = item.getItemId();
		final MercenaryTicket ticket = castle.getTicket(itemId);

		// Valid ticket for castle check.
		if (ticket == null) {
			activeChar.sendPacket(SystemMessageId.MERCENARIES_CANNOT_BE_POSITIONED_HERE);
			return;
		}

		// Siege in progress check.
		if (castle.getSiege().isInProgress()) {
			activeChar.sendPacket(SystemMessageId.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE);
			return;
		}

		// Seal validation check.
		if (!SevenSigns.getInstance().isSealValidationPeriod()) {
			activeChar.sendPacket(SystemMessageId.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE);
			return;
		}

		// Seal of Strife owner check.
		if (!ticket.isSsqType(SevenSigns.getInstance().getSealOwner(SealType.STRIFE))) {
			activeChar.sendPacket(SystemMessageId.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE);
			return;
		}

		// Max amount check.
		if (castle.getDroppedTicketsCount(itemId) >= ticket.getMaxAmount()) {
			activeChar.sendPacket(SystemMessageId.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE);
			return;
		}

		// Distance check.
		if (castle.isTooCloseFromDroppedTicket(activeChar.getX(), activeChar.getY(), activeChar.getZ())) {
			activeChar.sendPacket(SystemMessageId.POSITIONING_CANNOT_BE_DONE_BECAUSE_DISTANCE_BETWEEN_MERCENARIES_TOO_SHORT);
			return;
		}

		final ItemInstance droppedTicket = activeChar.dropItem("Consume", item.getObjectId(), 1, activeChar.getX(), activeChar.getY(), activeChar.getZ(), null, false);
		if (droppedTicket == null) {
			return;
		}

		castle.addDroppedTicket(droppedTicket);

		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PLACE_S1_IN_CURRENT_LOCATION_AND_DIRECTION).addItemName(itemId));
	}
}
