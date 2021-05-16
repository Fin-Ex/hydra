package sf.l2j.gameserver.data;

import java.io.File;
import sf.finex.enums.ETownType;
import sf.l2j.gameserver.instancemanager.CastleManager;
import sf.l2j.gameserver.instancemanager.ClanHallManager;
import sf.l2j.gameserver.instancemanager.ZoneManager;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.entity.Castle;
import sf.l2j.gameserver.model.entity.ClanHall;
import sf.l2j.gameserver.model.entity.Siege;
import sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import sf.l2j.gameserver.model.location.Location;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.model.zone.type.L2ArenaZone;
import sf.l2j.gameserver.model.zone.type.L2ClanHallZone;
import sf.l2j.gameserver.model.zone.type.L2TownZone;
import sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MapRegionTable {

	public static enum TeleportType {
		CASTLE,
		CLAN_HALL,
		SIEGE_FLAG,
		TOWN
	}

	private static Logger _log = LoggerFactory.getLogger(MapRegionTable.class.getName());

	private static final int REGIONS_X = 11;
	private static final int REGIONS_Y = 16;

	private static final Location MDT_LOCATION = new Location(12661, 181687, -3560);

	private final int[][] _regions = new int[REGIONS_X][REGIONS_Y];

	protected MapRegionTable() {
		int count = 0;

		try {
			File f = new File("./data/xml/map_region.xml");
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);

			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
				if (d.getNodeName().equalsIgnoreCase("map")) {
					NamedNodeMap attrs = d.getAttributes();
					int rY = Integer.valueOf(attrs.getNamedItem("geoY").getNodeValue()) - 10;
					for (int rX = 0; rX < REGIONS_X; rX++) {
						_regions[rX][rY] = Integer.valueOf(attrs.getNamedItem("geoX_" + (rX + 16)).getNodeValue());
						count++;
					}
				}
			}
		} catch (Exception e) {
			_log.warn("MapRegionTable: Error while loading \"map_region.xml\".", e);
		}
		_log.info("MapRegionTable: Loaded " + count + " regions.");
	}

	public final int getMapRegion(int posX, int posY) {
		return _regions[getMapRegionX(posX)][getMapRegionY(posY)];
	}

	public static final int getMapRegionX(int posX) {
		// +4 to shift coords center
		return (posX >> 15) + 4;
	}

	public static final int getMapRegionY(int posY) {
		// +8 to shift coords center
		return (posY >> 15) + 8;
	}

	/**
	 * @param x
	 * @param y
	 * @return the castle id associated to the town, based on X/Y points.
	 */
	public final int getAreaCastle(int x, int y) {
		switch (getMapRegion(x, y)) {
			case 0: // Talking Island Village
			case 5: // Town of Gludio
			case 6: // Gludin Village
				return 1;

			case 7: // Town of Dion
				return 2;

			case 8: // Town of Giran
			case 12: // Giran Harbor
				return 3;

			case 1: // Elven Village
			case 2: // Dark Elven Village
			case 9: // Town of Oren
			case 17: // Floran Village
				return 4;

			case 10: // Town of Aden
			case 11: // Hunters Village
			default: // Town of Aden
				return 5;

			case 13: // Heine
				return 6;

			case 15: // Town of Goddard
				return 7;

			case 14: // Rune Township
			case 18: // Primeval Isle Wharf
				return 8;

			case 3: // Orc Village
			case 4: // Dwarven Village
			case 16: // Town of Schuttgart
				return 9;
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return a String consisting of town name, based on X/Y points.
	 */
	public String getClosestTownName(int x, int y) {
		return getClosestTownName(getMapRegion(x, y));
	}

	public String getClosestTownName(int townId) {
		switch (townId) {
			case 0:
				return "Talking Island Village";

			case 1:
				return "Elven Village";

			case 2:
				return "Dark Elven Village";

			case 3:
				return "Orc Village";

			case 4:
				return "Dwarven Village";

			case 5:
				return "Town of Gludio";

			case 6:
				return "Gludin Village";

			case 7:
				return "Town of Dion";

			case 8:
				return "Town of Giran";

			case 9:
				return "Town of Oren";

			case 10:
				return "Town of Aden";

			case 11:
				return "Hunters Village";

			case 12:
				return "Giran Harbor";

			case 13:
				return "Heine";

			case 14:
				return "Rune Township";

			case 15:
				return "Town of Goddard";

			case 16:
				return "Town of Schuttgart";

			case 17:
				return "Floran Village";

			case 18:
				return "Primeval Isle";

			default:
				return "Town of Aden";
		}
	}

	/**
	 * @param character : The type of character to check.
	 * @param teleportType : The type of teleport to check.
	 * @return a Location based on character and teleport types.
	 */
	public Location getLocationToTeleport(Creature character, TeleportType teleportType) {
		// The character isn't a player, bypass all checks and retrieve a random spawn location on closest town.
		if (!(character instanceof Player)) {
			return getClosestTown(character.getX(), character.getY()).getSpawnLoc();
		}

		final Player player = ((Player) character);

		// The player is in MDT, move him out.
		if (player.isInsideZone(ZoneId.MONSTER_TRACK)) {
			return MDT_LOCATION;
		}

		if (teleportType != TeleportType.TOWN && player.getClan() != null) {
			if (null != teleportType) {
				switch (teleportType) {
					case CLAN_HALL:
						final ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(player.getClan());
						if (ch != null) {
							final L2ClanHallZone zone = ch.getZone();
							if (zone != null) {
								return zone.getSpawnLoc();
							}
						}
						break;
					case CASTLE:
						// Check if the player is part of a castle owning clan.
						Castle castle = CastleManager.getInstance().getCastleByOwner(player.getClan());
						if (castle == null) {
							// If not, check if he is in defending side.
							castle = CastleManager.getInstance().getCastle(player);
							if (!(castle != null && castle.getSiege().isInProgress() && castle.getSiege().checkSides(player.getClan(), SiegeSide.DEFENDER, SiegeSide.OWNER))) {
								castle = null;
							}
						}
						if (castle != null && castle.getCastleId() > 0) {
							return castle.getCastleZone().getSpawnLoc();
						}
						break;
					case SIEGE_FLAG:
						final Siege siege = CastleManager.getInstance().getSiege(player);
						if (siege != null) {
							final Npc flag = siege.getFlag(player.getClan());
							if (flag != null) {
								return flag.getPosition();
							}
						}
						break;
					default:
						break;
				}
			}
		}

		// Check if the player needs to be teleported in second closest town, during an active siege.
		final Castle castle = CastleManager.getInstance().getCastle(player);
		if (castle != null && castle.getSiege().isInProgress()) {
			return (player.getKarma() > 0) ? castle.getSiegeZone().getChaoticSpawnLoc() : castle.getSiegeZone().getSpawnLoc();
		}

		// Karma player lands out of city.
		if (player.getKarma() > 0) {
			return getClosestTown(player).getChaoticSpawnLoc();
		}

		// Check if player is in arena.
		final L2ArenaZone arena = ZoneManager.getInstance().getZone(player, L2ArenaZone.class);
		if (arena != null) {
			return arena.getSpawnLoc();
		}

		// Retrieve a random spawn location of the nearest town.
		return getClosestTown(player).getSpawnLoc();
	}

	/**
	 * A specific method, used ONLY by players. There's a Race condition.
	 *
	 * @param player : The player used to find race, x and y.
	 * @return the closest L2TownZone based on a X/Y location.
	 */
	private L2TownZone getClosestTown(Player player) {
		final int region = getMapRegion(player.getX(), player.getY());
		for (ETownType town : ETownType.VALUES) {
			if (town.checkRegion(region)) {
				return getTown(town);
			}
		}
		return getTown(ETownType.Floran);
	}

	/**
	 * @param x : The current character's X location.
	 * @param y : The current character's Y location.
	 * @return the closest L2TownZone based on a X/Y location.
	 */
	private L2TownZone getClosestTown(int x, int y) {
		final int region = getMapRegion(x, y);
		for (ETownType town : ETownType.VALUES) {
			if (town.checkRegion(region)) {
				return getTown(town);
			}
		}
		return getTown(ETownType.Floran);
	}

//	/**
//	 * @param x : The current character's X location.
//	 * @param y : The current character's Y location.
//	 * @return the second closest L2TownZone based on a X/Y location.
//	 */
//	public final L2TownZone getSecondClosestTown(int x, int y) {
//		final int region = getMapRegion(x, y);
//		switch (getMapRegion(x, y)) {
//			case 0: // TI
//			case 1: // Elven
//			case 2: // DE
//			case 5: // Gludio
//			case 6: // Gludin
//				return getTown(5);
//
//			case 3: // Orc
//				return getTown(4);
//
//			case 4: // Dwarven
//			case 16: // Schuttgart
//				return getTown(6);
//
//			case 7: // Dion
//				return getTown(7);
//
//			case 8: // Giran
//			case 9: // Oren
//			case 10:// Aden
//			case 11: // HV
//				return getTown(11);
//
//			case 12: // Giran Harbour
//			case 13: // Heine
//			case 17:// Floran
//				return getTown(16);
//
//			case 14: // Rune
//				return getTown(13);
//
//			case 15: // Goddard
//				return getTown(12);
//
//			case 18: // Primeval Isle
//				return getTown(18);
//		}
//		return getTown(16); // Default to floran
//	}
	/**
	 * @param x : The current character's X location.
	 * @param y : The current character's Y location.
	 * @return the closest region based on a X/Y location.
	 */
	public final int getClosestLocation(int x, int y) {
		switch (getMapRegion(x, y)) {
			case 0: // TI
				return 1;

			case 1: // Elven
				return 4;

			case 2: // DE
				return 3;

			case 3: // Orc
			case 4: // Dwarven
			case 16:// Schuttgart
				return 9;

			case 5: // Gludio
			case 6: // Gludin
				return 2;

			case 7: // Dion
				return 5;

			case 8: // Giran
			case 12: // Giran Harbor
				return 6;

			case 9: // Oren
				return 10;

			case 10: // Aden
				return 13;

			case 11: // HV
				return 11;

			case 13: // Heine
				return 12;

			case 14: // Rune
				return 14;

			case 15: // Goddard
				return 15;
		}
		return 0;
	}

	public static final L2TownZone getTown(ETownType townType) {
		for (L2TownZone temp : ZoneManager.getInstance().getAllZones(L2TownZone.class)) {
			if (temp.getTownId() == townType.getId()) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * @param x coords to check.
	 * @param y coords to check.
	 * @param z coords to check.
	 * @return a L2TownZone based on the overall list of zones, matching a 3D
	 * location.
	 */
	public static final L2TownZone getTown(int x, int y, int z) {
		return ZoneManager.getInstance().getZone(x, y, z, L2TownZone.class);
	}

	public static MapRegionTable getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		protected static final MapRegionTable INSTANCE = new MapRegionTable();
	}
}
