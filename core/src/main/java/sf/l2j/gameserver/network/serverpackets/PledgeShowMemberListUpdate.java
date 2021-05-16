package sf.l2j.gameserver.network.serverpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.pledge.ClanMember;

/**
 * @author -Wooden-
 */
public final class PledgeShowMemberListUpdate extends L2GameServerPacket {

	private final int _pledgeType;
	private final int _hasSponsor;
	private final String _name;
	private final int _level;
	private final int _classId;
	private final int _isOnline;
	private final int _race;
	private final int _sex;

	public PledgeShowMemberListUpdate(Player player) {
		_pledgeType = player.getPledgeType();
		_hasSponsor = (player.getSponsor() != 0 || player.getApprentice() != 0) ? 1 : 0;
		_name = player.getName();
		_level = player.getLevel();
		_classId = player.getClassId().getId();
		_race = player.getRace().ordinal();
		_sex = player.getAppearance().getSex().ordinal();
		_isOnline = (player.isOnline()) ? player.getObjectId() : 0;
	}

	public PledgeShowMemberListUpdate(ClanMember player) {
		_name = player.getName();
		_level = player.getLevel();
		_classId = player.getClassId();
		_isOnline = (player.isOnline()) ? player.getObjectId() : 0;
		_pledgeType = player.getPledgeType();
		_hasSponsor = (player.getSponsor() != 0 || player.getApprentice() != 0) ? 1 : 0;

		if (_isOnline != 0) {
			_race = player.getPlayerInstance().getRace().ordinal();
			_sex = player.getPlayerInstance().getAppearance().getSex().ordinal();
		} else {
			_sex = 0;
			_race = 0;
		}
	}

	@Override
	protected final void writeImpl() {
		writeC(0x54);
		writeS(_name);
		writeD(_level);
		writeD(_classId);
		writeD(_sex);
		writeD(_race);
		writeD(_isOnline);
		writeD(_pledgeType);
		writeD(_hasSponsor);
	}
}
