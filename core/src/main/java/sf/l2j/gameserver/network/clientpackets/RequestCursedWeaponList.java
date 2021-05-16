package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.ExCursedWeaponList;

public class RequestCursedWeaponList extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		activeChar.sendPacket(new ExCursedWeaponList(CursedWeaponsManager.getInstance().getCursedWeaponsIds()));
	}
}
