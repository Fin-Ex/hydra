package net.sf.l2j.gameserver.model.zone.type;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.data.MapRegionTable.TeleportType;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;

/**
 * @author DaRkRaGe
 */
public class L2BossZone extends L2ZoneType {

	private static final String SELECT_GRAND_BOSS_LIST = "SELECT * FROM grandboss_list WHERE zone = ?";

	// Track the times that players got disconnected. Players are allowed to log back into the zone as long as their log-out was within _timeInvade time...
	private final Map<Integer, Long> _playerAllowEntry = new ConcurrentHashMap<>();

	// Track players admitted to the zone who should be allowed back in after reboot/server downtime, within 30min of server restart
	private final List<Integer> _playerAllowed = new CopyOnWriteArrayList<>();

	private int _timeInvade;
	private boolean _enabled = true;
	private final int[] _oustLoc = new int[3];

	public L2BossZone(int id) {
		super(id);

		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement(SELECT_GRAND_BOSS_LIST)) {
			statement.setInt(1, id);
			try (ResultSet rset = statement.executeQuery()) {
				while (rset.next()) {
					allowPlayerEntry(rset.getInt("player_id"));
				}
			}
		} catch (Exception e) {
			log.warn("L2BossZone: Could not load grandboss zone id=" + id + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void setParameter(String name, String value) {
		switch (name) {
			case "InvadeTime":
				_timeInvade = Integer.parseInt(value);
				break;
			case "EnabledByDefault":
				_enabled = Boolean.parseBoolean(value);
				break;
			case "oustX":
				_oustLoc[0] = Integer.parseInt(value);
				break;
			case "oustY":
				_oustLoc[1] = Integer.parseInt(value);
				break;
			case "oustZ":
				_oustLoc[2] = Integer.parseInt(value);
				break;
			default:
				super.setParameter(name, value);
				break;
		}
	}

	@Override
	protected void onEnter(Creature character) {
		if (_enabled) {
			if (character instanceof Player) {
				// Get player and set zone info.
				final Player player = (Player) character;
				player.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);

				// Skip other checks for GM.
				if (player.isGM()) {
					return;
				}

				// Get player object id.
				final int id = player.getObjectId();

				if (_playerAllowed.contains(id)) {
					// Get and remove the entry expiration time (once entered, can not enter enymore, unless specified).
					final long entryTime = _playerAllowEntry.remove(id);
					if (entryTime > System.currentTimeMillis()) {
						return;
					}

					// Player trying to join after expiration, remove from allowed list.
					_playerAllowed.remove(Integer.valueOf(id));
				}

				// Teleport out player, who attempt "illegal" (re-)entry.
				if (_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0) {
					player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2], 0);
				} else {
					player.teleToLocation(TeleportType.TOWN);
				}
			} else if (character instanceof Summon) {
				final Player player = ((Summon) character).getPlayer();
				if (player != null) {
					if (_playerAllowed.contains(player.getObjectId()) || player.isGM()) {
						return;
					}

					// Teleport out owner who attempt "illegal" (re-)entry.
					if (_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0) {
						player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2], 0);
					} else {
						player.teleToLocation(TeleportType.TOWN);
					}
				}

				// Remove summon.
				((Summon) character).unSummon(player);
			}
		}
	}

	@Override
	protected void onExit(Creature character) {
		if (character instanceof Playable && _enabled) {
			if (character instanceof Player) {
				// Get player and set zone info.
				final Player player = (Player) character;
				player.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);

				// Skip other checks for GM.
				if (player.isGM()) {
					return;
				}

				// Get player object id.
				final int id = player.getObjectId();

				if (_playerAllowed.contains(id)) {
					if (!player.isOnline()) {
						// Player disconnected.
						_playerAllowEntry.put(id, System.currentTimeMillis() + _timeInvade);
					} else {
						// Player has allowed entry, do not delete from allowed list.
						if (_playerAllowEntry.containsKey(id)) {
							return;
						}

						// Remove player allowed list.
						_playerAllowed.remove(Integer.valueOf(id));
					}
				}
			}

			// If playables aren't found, force all bosses to return to spawnpoint.
			if (!_characterList.isEmpty()) {
				if (!getKnownTypeInside(Playable.class).isEmpty()) {
					return;
				}

				for (Attackable raid : getKnownTypeInside(Attackable.class)) {
					if (!raid.isRaid()) {
						continue;
					}

					raid.returnHome(true);
				}
			}
		} else if (character instanceof Attackable && character.isRaid()) {
			((Attackable) character).returnHome(true);
		}
	}

	/**
	 * Enables the entry of a player to the boss zone for next "duration"
	 * seconds. If the player tries to enter the boss zone after this period, he
	 * will be teleported out.
	 *
	 * @param player : Player to allow entry.
	 * @param duration : Entry permission is valid for this period (in seconds).
	 */
	public void allowPlayerEntry(Player player, int duration) {
		// Get player object id.
		final int playerId = player.getObjectId();

		// Allow player entry.
		if (!_playerAllowed.contains(playerId)) {
			_playerAllowed.add(playerId);
		}

		// For the given duration.
		_playerAllowEntry.put(playerId, System.currentTimeMillis() + duration * 1000);
	}

	/**
	 * Enables the entry of a player to the boss zone after server
	 * shutdown/restart. The time limit is specified by each zone via
	 * "InvadeTime" parameter. If the player tries to enter the boss zone after
	 * this period, he will be teleported out.
	 *
	 * @param playerId : The ID of player to allow entry.
	 */
	public void allowPlayerEntry(int playerId) {
		// Allow player entry.
		if (!_playerAllowed.contains(playerId)) {
			_playerAllowed.add(playerId);
		}

		// For the given duration.
		_playerAllowEntry.put(playerId, System.currentTimeMillis() + _timeInvade);
	}

	/**
	 * Removes the player from allowed list and cancel the entry permition.
	 *
	 * @param player : Player to remove from the zone.
	 */
	public void removePlayer(Player player) {
		// Get player object id.
		final int id = player.getObjectId();

		// Remove player from allowed list.
		_playerAllowed.remove(Integer.valueOf(id));

		// Remove player permission.
		_playerAllowEntry.remove(id);
	}

	/**
	 * @return the list of all allowed players object ids.
	 */
	public List<Integer> getAllowedPlayers() {
		return _playerAllowed;
	}

	/**
	 * Some GrandBosses send all players in zone to a specific part of the zone,
	 * rather than just removing them all. If this is the case, this command
	 * should be used. If this is no the case, then use oustAllPlayers().
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void movePlayersTo(int x, int y, int z) {
		if (_characterList.isEmpty()) {
			return;
		}

		for (Player player : getKnownTypeInside(Player.class)) {
			if (player.isOnline()) {
				player.teleToLocation(x, y, z, 0);
			}
		}
	}

	/**
	 * Occasionally, all players need to be sent out of the zone (for example,
	 * if the players are just running around without fighting for too long, or
	 * if all players die, etc). This call sends all online players to town and
	 * marks offline players to be teleported (by clearing their relog
	 * expiration times) when they log back in (no real need for off-line
	 * teleport).
	 */
	public void oustAllPlayers() {
		if (_characterList.isEmpty()) {
			return;
		}

		for (Player player : getKnownTypeInside(Player.class)) {
			if (player.isOnline()) {
				if (_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0) {
					player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2], 0);
				} else {
					player.teleToLocation(TeleportType.TOWN);
				}
			}
		}
		_playerAllowEntry.clear();
		_playerAllowed.clear();
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}
}
