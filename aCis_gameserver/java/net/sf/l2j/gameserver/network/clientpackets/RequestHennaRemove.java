package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.model.dye.DyeComponent;
import net.sf.finex.data.DyeData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 * format cd
 */
public final class RequestHennaRemove extends L2GameClientPacket {

	private int _symbolId;

	@Override
	protected void readImpl() {
		_symbolId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final DyeComponent dyeComponent = activeChar.getComponent(DyeComponent.class);
		for (int i = 0; i < 3; i++) {
			final DyeData dye = dyeComponent.getDye(i);
			if (dye != null && dye.getSymbolId() == _symbolId) {
				if (activeChar.getAdena() >= (dye.getPrice() / 5)) {
					dyeComponent.removeDye(i);
					break;
				}
				activeChar.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
			}
		}
	}
}
