package net.sf.l2j.gameserver.network.serverpackets;


import net.sf.finex.model.GLT.GLTController;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.pledge.Clan;

public class Die extends L2GameServerPacket {

	private final Creature _activeChar;
	private final int _charObjId;
	private final boolean _fake;

	private boolean _sweepable;
	private boolean _allowFixedRes;
	private Clan _clan;
	private boolean isGLTParticipant;

	public Die(Creature cha) {
		_activeChar = cha;
		_charObjId = cha.getObjectId();
		_fake = !cha.isDead();

		if (cha.isPlayer()) {
			final Player player = cha.getPlayer();
			_allowFixedRes = player.getAccessLevel().allowFixedRes();
			_clan = player.getClan();
			isGLTParticipant = GLTController.getInstance().isParticipate(player);
		} else if (cha.isAttackableInstance()) {
			_sweepable = cha.getAttackable().isSpoiled();
		}
	}

	@Override
	protected final void writeImpl() {
		if (_fake) {
			return;
		}

		writeC(0x06);
		writeD(_charObjId);
		if(isGLTParticipant) {
			// GLT players can't respawn in any points, only after N seconds
			writeD(0x00); // to nearest village
			writeD(0x00); // to clanhall
			writeD(0x00); // to castle
			writeD(0x00); // to siege HQ
			writeD(_sweepable ? 0x01 : 0x00); // sweepable (blue glow)
			writeD(0x00); // FIXED resurrection off
		} else {
			writeD(0x01); // to nearest village

			if (_clan != null) {
				SiegeSide side = null;

				final Siege siege = CastleManager.getInstance().getSiege(_activeChar);
				if (siege != null) {
					side = siege.getSide(_clan);
				}

				writeD((_clan.hasHideout()) ? 0x01 : 0x00); // to clanhall
				writeD((_clan.hasCastle() || side == SiegeSide.OWNER || side == SiegeSide.DEFENDER) ? 0x01 : 0x00); // to castle
				writeD((side == SiegeSide.ATTACKER && _clan.getFlag() != null) ? 0x01 : 0x00); // to siege HQ
			} else {
				writeD(0x00); // to clanhall
				writeD(0x00); // to castle
				writeD(0x00); // to siege HQ
			}
			writeD(_sweepable ? 0x01 : 0x00); // sweepable (blue glow)
			writeD(_allowFixedRes ? 0x01 : 0x00); // FIXED
		}
	}
}
