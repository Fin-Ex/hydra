package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;

/**
 * @author Luca Baldi
 */
public class EtcStatusUpdate extends L2GameServerPacket {

	private final Player _activeChar;

	public EtcStatusUpdate(Player activeChar) {
		_activeChar = activeChar;
	}

	@Override
	protected void writeImpl() {
		writeC(0xF3);
		writeD(_activeChar.getCharges());
		writeD(_activeChar.getWeightPenalty());
		writeD((_activeChar.isInRefusalMode() || _activeChar.isChatBanned()) ? 1 : 0);
		writeD(_activeChar.isInsideZone(ZoneId.DANGER_AREA) ? 1 : 0);
		writeD((_activeChar.getExpertiseWeaponPenalty() || _activeChar.getExpertiseArmorPenalty() > 0) ? 1 : 0);
		writeD(_activeChar.isAffected(EEffectFlag.CHARM_OF_COURAGE) ? 1 : 0);
		writeD(_activeChar.getDeathPenaltyBuffLevel());
	}
}
