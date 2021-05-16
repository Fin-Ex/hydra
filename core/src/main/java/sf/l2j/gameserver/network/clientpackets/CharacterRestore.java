package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.network.FloodProtectors;
import sf.l2j.gameserver.network.FloodProtectors.Action;
import sf.l2j.gameserver.network.serverpackets.CharSelectInfo;

public final class CharacterRestore extends L2GameClientPacket {

	private int _charSlot;

	@Override
	protected void readImpl() {
		_charSlot = readD();
	}

	@Override
	protected void runImpl() {
		if (!FloodProtectors.performAction(getClient(), Action.CHARACTER_SELECT)) {
			return;
		}

		try {
			getClient().markRestoredChar(_charSlot);
		} catch (Exception e) {
		}

		CharSelectInfo cl = new CharSelectInfo(getClient().getAccountName(), getClient().getSessionId().playOkID1, 0);
		sendPacket(cl);
		getClient().setCharSelection(cl.getCharInfo());
	}
}
