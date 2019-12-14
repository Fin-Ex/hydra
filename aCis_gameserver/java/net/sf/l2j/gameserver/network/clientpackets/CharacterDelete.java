package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import java.util.logging.Level;

import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.serverpackets.CharDeleteFail;
import net.sf.l2j.gameserver.network.serverpackets.CharDeleteOk;
import net.sf.l2j.gameserver.network.serverpackets.CharSelectInfo;

public final class CharacterDelete extends L2GameClientPacket {

	private int _charSlot;

	@Override
	protected void readImpl() {
		_charSlot = readD();
	}

	@Override
	protected void runImpl() {
		if (!FloodProtectors.performAction(getClient(), Action.CHARACTER_SELECT)) {
			sendPacket(new CharDeleteFail(CharDeleteFail.REASON_DELETION_FAILED));
			return;
		}

		try {
			byte answer = getClient().markToDeleteChar(_charSlot);

			switch (answer) {
				default:
				case -1: // Error
					break;
				case 0: // Success!
					sendPacket(CharDeleteOk.STATIC_PACKET);
					break;
				case 1:
					sendPacket(new CharDeleteFail(CharDeleteFail.REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER));
					break;
				case 2:
					sendPacket(new CharDeleteFail(CharDeleteFail.REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED));
					break;
			}
		} catch (Exception e) {
			_log.error("Error:", e);
		}

		CharSelectInfo cl = new CharSelectInfo(getClient().getAccountName(), getClient().getSessionId().playOkID1, 0);
		sendPacket(cl);
		getClient().setCharSelection(cl.getCharInfo());
	}
}
