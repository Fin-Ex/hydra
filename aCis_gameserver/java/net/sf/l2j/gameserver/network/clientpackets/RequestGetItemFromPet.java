package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Pet;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.EnchantResult;

public final class RequestGetItemFromPet extends L2GameClientPacket {

	private int _objectId;
	private int _amount;
	@SuppressWarnings("unused")
	private int _unknown;

	@Override
	protected void readImpl() {
		_objectId = readD();
		_amount = readD();
		_unknown = readD();// = 0 for most trades
	}

	@Override
	protected void runImpl() {
		if (_amount <= 0) {
			return;
		}

		final Player player = getClient().getActiveChar();
		if (player == null || !player.hasPet()) {
			return;
		}

		if (player.isProcessingTransaction()) {
			player.sendPacket(SystemMessageId.ALREADY_TRADING);
			return;
		}

		if (player.getActiveEnchantItem() != null) {
			player.setActiveEnchantItem(null);
			player.sendPacket(EnchantResult.CANCELLED);
			player.sendPacket(SystemMessageId.ENCHANT_SCROLL_CANCELLED);
		}

		final Pet pet = (Pet) player.getActiveSummon();

		pet.transferItem("Transfer", _objectId, _amount, player.getInventory(), player, pet);
	}
}
