package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.Config;
import sf.l2j.commons.concurrent.ThreadPool;
import sf.l2j.gameserver.data.MapRegionTable;
import sf.l2j.gameserver.data.MapRegionTable.TeleportType;
import sf.l2j.gameserver.instancemanager.CastleManager;
import sf.l2j.gameserver.instancemanager.ClanHallManager;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.entity.ClanHall;
import sf.l2j.gameserver.model.entity.ClanHall.ClanHallFunction;
import sf.l2j.gameserver.model.entity.Siege;
import sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import sf.l2j.gameserver.model.location.Location;
import sf.l2j.gameserver.model.pledge.Clan;

public final class RequestRestartPoint extends L2GameClientPacket {

	protected static final Location JAIL_LOCATION = new Location(-114356, -249645, -2984);

	protected int _requestType;

	@Override
	protected void readImpl() {
		_requestType = readD();
	}

	class DeathTask implements Runnable {

		final Player _player;

		DeathTask(Player player) {
			_player = player;
		}

		@Override
		public void run() {
			final Clan clan = _player.getClan();

			Location loc = null;

			// Enforce type.
			if (_player.isInJail()) {
				_requestType = 27;
			} else if (_player.isFestivalParticipant()) {
				_requestType = 4;
			}

			// To clanhall.
			switch (_requestType) {
				// To castle.
				case 1:
					if (clan == null || !clan.hasHideout()) {
						_log.warn(_player.getName() + " called RestartPointPacket - To Clanhall while he doesn't have clan / Clanhall.");
						return;
					}
					loc = MapRegionTable.getInstance().getLocationToTeleport(_player, TeleportType.CLAN_HALL);
					final ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
					if (ch != null) {
						final ClanHallFunction function = ch.getFunction(ClanHall.FUNC_RESTORE_EXP);
						if (function != null) {
							_player.restoreExp(function.getLvl());
						}
					}
					break;
				// To siege flag.
				case 2:
					final Siege siege = CastleManager.getInstance().getSiege(_player);
					if (siege != null) {
						switch (siege.getSide(clan)) {
							case DEFENDER:
							case OWNER:
								loc = MapRegionTable.getInstance().getLocationToTeleport(_player, TeleportType.CASTLE);
								break;

							case ATTACKER:
								loc = MapRegionTable.getInstance().getLocationToTeleport(_player, TeleportType.TOWN);
								break;

							default:
								_log.warn(_player.getName() + " called RestartPointPacket - To Castle while he isn't registered to any castle siege.");
								return;
						}
					} else {
						if (clan == null || !clan.hasCastle()) {
							return;
						}

						loc = MapRegionTable.getInstance().getLocationToTeleport(_player, TeleportType.CASTLE);
					}
					break;
				case 3:
					loc = MapRegionTable.getInstance().getLocationToTeleport(_player, TeleportType.SIEGE_FLAG);
					// Fixed.
					break;
				// To jail.
				case 4:
					if (!_player.isGM() && !_player.isFestivalParticipant()) {
						_log.warn(_player.getName() + " called RestartPointPacket - Fixed while he isn't festival participant!");
						return;
					}
					loc = _player.getPosition();
					break;
				// Nothing has been found, use regular "To town" behavior.
				case 27:
					if (!_player.isInJail()) {
						return;
					}
					loc = JAIL_LOCATION;
					break;
				default:
					loc = MapRegionTable.getInstance().getLocationToTeleport(_player, TeleportType.TOWN);
					break;
			}

			_player.setIsIn7sDungeon(false);

			if (_player.isDead()) {
				_player.doRevive();
			}

			_player.teleToLocation(loc, 20);
		}
	}

	@Override
	protected void runImpl() {
		final Player player = getClient().getActiveChar();
		if (player == null) {
			return;
		}

		if (player.isFakeDeath()) {
			player.stopFakeDeath(true);
			return;
		}

		if (!player.isDead()) {
			_log.warn("Living player [" + player.getName() + "] called RequestRestartPoint packet.");
			return;
		}

		// Schedule a respawn delay if player is part of a clan registered in an active siege.
		if (player.getClan() != null) {
			final Siege siege = CastleManager.getInstance().getSiege(player);
			if (siege != null && siege.checkSide(player.getClan(), SiegeSide.ATTACKER)) {
				ThreadPool.schedule(new DeathTask(player), Config.ATTACKERS_RESPAWN_DELAY);

				if (Config.ATTACKERS_RESPAWN_DELAY > 0) {
					player.sendMessage("You will be teleported in " + Config.ATTACKERS_RESPAWN_DELAY / 1000 + " seconds.");
				}

				return;
			}
		}

		// Run the task immediately (no need to schedule).
		new DeathTask(player).run();
	}
}
