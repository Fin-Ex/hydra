package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.cache.CrestCache;
import sf.l2j.gameserver.cache.CrestCache.CrestType;
import sf.l2j.gameserver.data.sql.ClanTable;
import sf.l2j.gameserver.idfactory.IdFactory;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.pledge.Clan;

public final class RequestSetAllyCrest extends L2GameClientPacket {

	private int _length;
	private byte[] _data;

	@Override
	protected void readImpl() {
		_length = readD();
		if (_length > 192) {
			return;
		}

		_data = new byte[_length];
		readB(_data);
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (_length < 0) {
			activeChar.sendMessage("File transfer error.");
			return;
		}

		if (_length > 192) {
			activeChar.sendMessage("The crest file size was too big (max 192 bytes).");
			return;
		}

		if (activeChar.getAllyId() != 0) {
			Clan leaderclan = ClanTable.getInstance().getClan(activeChar.getAllyId());
			if (activeChar.getClanId() != leaderclan.getClanId() || !activeChar.isClanLeader()) {
				return;
			}

			boolean remove = false;
			if (_length == 0 || _data.length == 0) {
				remove = true;
			}

			int newId = 0;
			if (!remove) {
				newId = IdFactory.getInstance().getNextId();
			}

			if (!remove && !CrestCache.getInstance().saveCrest(CrestType.ALLY, newId, _data)) {
				_log.info("Error saving crest for ally " + leaderclan.getAllyName() + " [" + leaderclan.getAllyId() + "]");
				return;
			}

			leaderclan.changeAllyCrest(newId, false);
		}
	}
}
