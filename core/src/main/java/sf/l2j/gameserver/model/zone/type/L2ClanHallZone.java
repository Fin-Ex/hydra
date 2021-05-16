package sf.l2j.gameserver.model.zone.type;

import sf.l2j.gameserver.data.MapRegionTable.TeleportType;
import sf.l2j.gameserver.instancemanager.ClanHallManager;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.entity.ClanHall;
import sf.l2j.gameserver.model.zone.L2SpawnZone;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.network.serverpackets.ClanHallDecoration;

/**
 * A clan hall zone
 *
 * @author durgus
 */
public class L2ClanHallZone extends L2SpawnZone {

	private int _clanHallId;

	public L2ClanHallZone(int id) {
		super(id);
	}

	@Override
	public void setParameter(String name, String value) {
		if (name.equals("clanHallId")) {
			_clanHallId = Integer.parseInt(value);

			// Register self to the correct clan hall
			ClanHallManager.getInstance().getClanHallById(_clanHallId).setZone(this);
		} else {
			super.setParameter(name, value);
		}
	}

	@Override
	protected void onEnter(Creature character) {
		if (character instanceof Player) {
			// Set as in clan hall
			character.setInsideZone(ZoneId.CLAN_HALL, true);

			ClanHall clanHall = ClanHallManager.getInstance().getClanHallById(_clanHallId);
			if (clanHall == null) {
				return;
			}

			// Send decoration packet
			ClanHallDecoration deco = new ClanHallDecoration(clanHall);
			((Player) character).sendPacket(deco);
		}
	}

	@Override
	protected void onExit(Creature character) {
		if (character instanceof Player) {
			character.setInsideZone(ZoneId.CLAN_HALL, false);
		}
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}

	/**
	 * Removes all foreigners from the clan hall
	 *
	 * @param owningClanId
	 */
	public void banishForeigners(int owningClanId) {
		for (Player player : getKnownTypeInside(Player.class)) {
			if (player.getClanId() == owningClanId) {
				continue;
			}

			player.teleToLocation(TeleportType.TOWN);
		}
	}

	/**
	 * @return the clanHallId
	 */
	public int getClanHallId() {
		return _clanHallId;
	}
}
