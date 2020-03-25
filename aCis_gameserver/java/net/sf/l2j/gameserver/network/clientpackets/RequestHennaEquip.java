package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.model.dye.DyeComponent;
import net.sf.finex.data.DyeData;
import net.sf.finex.data.tables.DyeTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestHennaEquip extends L2GameClientPacket {

	private int symbolId;

	@Override
	protected void readImpl() {
		symbolId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final DyeData dye = DyeTable.getInstance().get(symbolId);
		if (dye == null) {
			return;
		}

		final DyeComponent dyeComponent = activeChar.getComponent(DyeComponent.class);
		if (dyeComponent.getEmptySlots() == 0) {
			activeChar.sendPacket(SystemMessageId.SYMBOLS_FULL);
			return;
		}

		final ItemInstance ownedDyes = activeChar.getInventory().getItemByItemId(dye.getDyeId());
		final int count = (ownedDyes == null) ? 0 : ownedDyes.getCount();

		if (count < DyeData.getRequiredDyeAmount()) {
			activeChar.sendPacket(SystemMessageId.CANT_DRAW_SYMBOL);
			return;
		}

//		boolean symbolCantBeDrawn = false;
//		if(dye.getCON() < 0 && dye.getCON() + (activeChar.getCON() + dyeComponent.getDyeCON()) <= 0) {
//			symbolCantBeDrawn = true;
//		} else if(dye.getDEX() < 0 && dye.getDEX() + (activeChar.getDEX()+ dyeComponent.getDyeDEX()) <= 0) {
//			symbolCantBeDrawn = true;
//		} else if(dye.getSTR() < 0 && dye.getSTR() + (activeChar.getSTR() + dyeComponent.getDyeSTR()) <= 0) {
//			symbolCantBeDrawn = true;
//		} else if(dye.getINT() < 0 && dye.getINT() + (activeChar.getINT() + dyeComponent.getDyeINT()) <= 0) {
//			symbolCantBeDrawn = true;
//		} else if(dye.getWIT() < 0 && dye.getWIT() + activeChar.getWIT() <= 0) {
//			symbolCantBeDrawn = true;
//		} else if(dye.getMEN() < 0 && dye.getMEN() + (activeChar.getMEN() + dyeComponent.getDyeMEN()) <= 0) {
//			symbolCantBeDrawn = true;
//		}
//		
//		if(symbolCantBeDrawn) {
//			activeChar.sendPacket(SystemMessageId.CANT_DRAW_SYMBOL);
//			return;
//		}
		// reduceAdena sends a message.
		if (!activeChar.reduceAdena("Henna", dye.getPrice(), activeChar.getCurrentFolkNPC(), true)) {
			return;
		}

		// destroyItemByItemId sends a message.
		if (!activeChar.destroyItemByItemId("Henna", dye.getDyeId(), DyeData.getRequiredDyeAmount(), activeChar, true)) {
			return;
		}

		dyeComponent.addDye(dye);
	}
}
