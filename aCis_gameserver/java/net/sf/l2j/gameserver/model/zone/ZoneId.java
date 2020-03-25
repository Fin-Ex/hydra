package net.sf.l2j.gameserver.model.zone;

/**
 * Zone Ids.
 *
 * @author Zoey76
 */
public enum ZoneId {
	PVP(0),
	PEACE(1),
	SIEGE(2),
	MOTHER_TREE(3),
	CLAN_HALL(4),
	NO_LANDING(5),
	WATER(6),
	JAIL(7),
	MONSTER_TRACK(8),
	CASTLE(9),
	SWAMP(10),
	NO_SUMMON_FRIEND(11),
	NO_STORE(12),
	TOWN(13),
	HQ(14),
	DANGER_AREA(15),
	CAST_ON_ARTIFACT(16),
	NO_RESTART(17),
	SCRIPT(18),
	GLT(19),
	GLT_BUSH(20);

	public static final ZoneId[] VALUES = values();
	
	private final int id;

	private ZoneId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
