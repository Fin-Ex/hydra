package ru.finex.ws.l2.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * This class defines all classes (ex : human fighter, darkFighter...) that a player can chose.
 * <ul>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId or null if this class is the root</li>
 * </ul>
 */
@Slf4j
@Getter
@RequiredArgsConstructor
@Deprecated
public enum ClassId implements IdEnum {
	HUMAN_FIGHTER(0, Race.HUMAN, "Human Fighter", null),
	WARRIOR(1, Race.HUMAN, "Warrior", HUMAN_FIGHTER),
	GLADIATOR(2, Race.HUMAN, "Gladiator", WARRIOR),
	WARLORD(3, Race.HUMAN, "Warlord", WARRIOR),
	KNIGHT(4, Race.HUMAN, "Human Knight", HUMAN_FIGHTER),
	PALADIN(5, Race.HUMAN, "Paladin", KNIGHT),
	DARK_AVENGER(6, Race.HUMAN, "Dark Avenger", KNIGHT),
	ROGUE(7, Race.HUMAN, "Rogue", HUMAN_FIGHTER),
	TREASURE_HUNTER(8, Race.HUMAN, "Treasure Hunter", ROGUE),
	HAWK_EYE(9, Race.HUMAN, "Hawkeye", ROGUE),
	HUMAN_MYSTIC(10, Race.HUMAN, "Human Mystic", null),
	HUMAN_WIZARD(11, Race.HUMAN, "Human Wizard", HUMAN_MYSTIC),
	SORCERER(12, Race.HUMAN, "Sorcerer", HUMAN_WIZARD),
	NECROMANCER(13, Race.HUMAN, "Necromancer", HUMAN_WIZARD),
	WARLOCK(14, Race.HUMAN, "Warlock", HUMAN_WIZARD),
	CLERIC(15, Race.HUMAN, "Cleric", HUMAN_MYSTIC),
	BISHOP(16, Race.HUMAN, "Bishop", CLERIC),
	PROPHET(17, Race.HUMAN, "Prophet", CLERIC),
	ELVEN_FIGHTER(18, Race.ELF, "Elven Fighter", null),
	ELVEN_KNIGHT(19, Race.ELF, "Elven Knight", ELVEN_FIGHTER),
	TEMPLE_KNIGHT(20, Race.ELF, "Temple Knight", ELVEN_KNIGHT),
	SWORD_SINGER(21, Race.ELF, "Sword Singer", ELVEN_KNIGHT),
	ELVEN_SCOUT(22, Race.ELF, "Elven Scout", ELVEN_FIGHTER),
	PLAINS_WALKER(23, Race.ELF, "Plains Walker", ELVEN_SCOUT),
	SILVER_RANGER(24, Race.ELF, "Silver Ranger", ELVEN_SCOUT),
	ELVEN_MYSTIC(25, Race.ELF, "Elven Mystic", null),
	ELVEN_WIZARD(26, Race.ELF, "Elven Wizard", ELVEN_MYSTIC),
	SPELL_SINGER(27, Race.ELF, "Spellsinger", ELVEN_WIZARD),
	ELEMENTAL_SUMMONER(28, Race.ELF, "Elemental Summoner", ELVEN_WIZARD),
	ELVEN_ORACLE(29, Race.ELF, "Elven Oracle", ELVEN_MYSTIC),
	ELVEN_ELDER(30, Race.ELF, "Elven Elder", ELVEN_ORACLE),
	DARK_FIGHTER(31, Race.DARK_ELF, "Dark Fighter", null),
	PALUS_KNIGHT(32, Race.DARK_ELF, "Palus Knight", DARK_FIGHTER),
	SHILLIEN_KNIGHT(33, Race.DARK_ELF, "Shillien Knight", PALUS_KNIGHT),
	BLADEDANCER(34, Race.DARK_ELF, "Bladedancer", PALUS_KNIGHT),
	ASSASSIN(35, Race.DARK_ELF, "Assassin", DARK_FIGHTER),
	ABYSS_WALKER(36, Race.DARK_ELF, "Abyss Walker", ASSASSIN),
	PHANTOM_RANGER(37, Race.DARK_ELF, "Phantom Ranger", ASSASSIN),
	DARK_MYSTIC(38, Race.DARK_ELF, "Dark Mystic", null),
	DARK_WIZARD(39, Race.DARK_ELF, "Dark Wizard", DARK_MYSTIC),
	SPELL_HOWLER(40, Race.DARK_ELF, "Spellhowler", DARK_WIZARD),
	PHANTOM_SUMMONER(41, Race.DARK_ELF, "Phantom Summoner", DARK_WIZARD),
	SHILLIEN_ORACLE(42, Race.DARK_ELF, "Shillien Oracle", DARK_MYSTIC),
	SHILLIEN_ELDER(43, Race.DARK_ELF, "Shillien Elder", SHILLIEN_ORACLE),
	ORC_FIGHTER(44, Race.ORC, "Orc Fighter", null),
	ORC_RAIDER(45, Race.ORC, "Orc Raider", ORC_FIGHTER),
	DESTROYER(46, Race.ORC, "Destroyer", ORC_RAIDER),
	MONK(47, Race.ORC, "Monk", ORC_FIGHTER),
	TYRANT(48, Race.ORC, "Tyrant", MONK),
	ORC_MYSTIC(49, Race.ORC, "Orc Mystic", null),
	ORC_SHAMAN(50, Race.ORC, "Orc Shaman", ORC_MYSTIC),
	OVERLORD(51, Race.ORC, "Overlord", ORC_SHAMAN),
	WARCRYER(52, Race.ORC, "Warcryer", ORC_SHAMAN),
	DWARVEN_FIGHTER(53, Race.DWARF, "Dwarven Fighter", null),
	SCAVENGER(54, Race.DWARF, "Scavenger", DWARVEN_FIGHTER),
	BOUNTY_HUNTER(55, Race.DWARF, "Bounty Hunter", SCAVENGER),
	ARTISAN(56, Race.DWARF, "Artisan", DWARVEN_FIGHTER),
	WARSMITH(57, Race.DWARF, "Warsmith", ARTISAN),
	DUMMY_1(58, null, "dummy 1", null),
	DUMMY_2(59, null, "dummy 2", null),
	DUMMY_3(60, null, "dummy 3", null),
	DUMMY_4(61, null, "dummy 4", null),
	DUMMY_5(62, null, "dummy 5", null),
	DUMMY_6(63, null, "dummy 6", null),
	DUMMY_7(64, null, "dummy 7", null),
	DUMMY_8(65, null, "dummy 8", null),
	DUMMY_9(66, null, "dummy 9", null),
	DUMMY_10(67, null, "dummy 10", null),
	DUMMY_11(68, null, "dummy 11", null),
	DUMMY_12(69, null, "dummy 12", null),
	DUMMY_13(70, null, "dummy 13", null),
	DUMMY_14(71, null, "dummy 14", null),
	DUMMY_15(72, null, "dummy 15", null),
	DUMMY_16(73, null, "dummy 16", null),
	DUMMY_17(74, null, "dummy 17", null),
	DUMMY_18(75, null, "dummy 18", null),
	DUMMY_19(76, null, "dummy 19", null),
	DUMMY_20(77, null, "dummy 20", null),
	DUMMY_21(78, null, "dummy 21", null),
	DUMMY_22(79, null, "dummy 22", null),
	DUMMY_23(80, null, "dummy 23", null),
	DUMMY_24(81, null, "dummy 24", null),
	DUMMY_25(82, null, "dummy 25", null),
	DUMMY_26(83, null, "dummy 26", null),
	DUMMY_27(84, null, "dummy 27", null),
	DUMMY_28(85, null, "dummy 28", null),
	DUMMY_29(86, null, "dummy 29", null),
	DUMMY_30(87, null, "dummy 30", null),
	DUELIST(88, Race.HUMAN, "Duelist", GLADIATOR),
	DREADNOUGHT(89, Race.HUMAN, "Dreadnought", WARLORD),
	PHOENIX_KNIGHT(90, Race.HUMAN, "Phoenix Knight", PALADIN),
	HELL_KNIGHT(91, Race.HUMAN, "Hell Knight", DARK_AVENGER),
	SAGITTARIUS(92, Race.HUMAN, "Sagittarius", HAWK_EYE),
	ADVENTURER(93, Race.HUMAN, "Adventurer", TREASURE_HUNTER),
	ARCHMAGE(94, Race.HUMAN, "Archmage", SORCERER),
	SOULTAKER(95, Race.HUMAN, "Soultaker", NECROMANCER),
	ARCANA_LORD(96, Race.HUMAN, "Arcana Lord", WARLOCK),
	CARDINAL(97, Race.HUMAN, "Cardinal", BISHOP),
	HIEROPHANT(98, Race.HUMAN, "Hierophant", PROPHET),
	EVAS_TEMPLAR(99, Race.ELF, "Eva's Templar", TEMPLE_KNIGHT),
	SWORD_MUSE(100, Race.ELF, "Sword Muse", SWORD_SINGER),
	WIND_RIDER(101, Race.ELF, "Wind Rider", PLAINS_WALKER),
	MOONLIGHT_SENTINEL(102, Race.ELF, "Moonlight Sentinel", SILVER_RANGER),
	MYSTIC_MUSE(103, Race.ELF, "Mystic Muse", SPELL_SINGER),
	ELEMENTAL_MASTER(104, Race.ELF, "Elemental Master", ELEMENTAL_SUMMONER),
	EVAS_SAINT(105, Race.ELF, "Eva's Saint", ELVEN_ELDER),
	SHILLIEN_TEMPLAR(106, Race.DARK_ELF, "Shillien Templar", SHILLIEN_KNIGHT),
	SPECTRAL_DANCER(107, Race.DARK_ELF, "Spectral Dancer", BLADEDANCER),
	GHOST_HUNTER(108, Race.DARK_ELF, "Ghost Hunter", ABYSS_WALKER),
	GHOST_SENTINEL(109, Race.DARK_ELF, "Ghost Sentinel", PHANTOM_RANGER),
	STORM_SCREAMER(110, Race.DARK_ELF, "Storm Screamer", SPELL_HOWLER),
	SPECTRAL_MASTER(111, Race.DARK_ELF, "Spectral Master", PHANTOM_SUMMONER),
	SHILLIEN_SAINT(112, Race.DARK_ELF, "Shillien Saint", SHILLIEN_ELDER),
	TITAN(113, Race.ORC, "Titan", DESTROYER),
	GRAND_KHAWATARI(114, Race.ORC, "Grand Khavatari", TYRANT),
	DOMINATOR(115, Race.ORC, "Dominator", OVERLORD),
	DOOMCRYER(116, Race.ORC, "Doom Cryer", WARCRYER),
	FORTUNE_SEEKER(117, Race.DWARF, "Fortune Seeker", BOUNTY_HUNTER),
	MAESTRO(118, Race.DWARF, "Maestro", WARSMITH),

	WORLD_TRAP(119, null, null, null),
	PC_TRAP(120, null, null, null),
	DOPPELGANGER(121, null, null, null),
	SIEGE_ATTACKER(122, null, null, null),

	MALE_SOLDIER(123, Race.KAMAEL, "Soldier", null),
	FEMALE_SOLDIER(124, Race.KAMAEL, "Soldier", null),
	TROOPER(125, Race.KAMAEL, "Trooper", MALE_SOLDIER),
	WARDER(126, Race.KAMAEL, "Warden", FEMALE_SOLDIER),
	BERSERKER(127, Race.KAMAEL, "Berserker", TROOPER),
	MALE_SOULBREAKER(128, Race.KAMAEL, "Soulbreaker", TROOPER),
	FEMALE_SOULBREAKER(129, Race.KAMAEL, "Soulbreaker", WARDER),
	ARBALESTER(130, Race.KAMAEL, "Arbalester", WARDER),
	DOOMBRINGER(131, Race.KAMAEL, "Doombringer", BERSERKER),
	MALE_SOUL_HOUND(132, Race.KAMAEL, "Soul Hound", MALE_SOULBREAKER),
	FEMALE_SOUL_HOUND(133, Race.KAMAEL, "Soul Hound", FEMALE_SOULBREAKER),
	TRICKSTER(134, Race.KAMAEL, "Trickster", ARBALESTER),
	INSPECTOR(135, Race.KAMAEL, "Inspector", WARDER),
	JUDICATOR(136, Race.KAMAEL, "Judicator", INSPECTOR),
	// skipped: 5th class levels
	ERTHEIA_FIGHTER(182, Race.ERTHEIA, "Fighter", null),
	ERTHEIA_WIZARD(183, Race.ERTHEIA, "Wizard", null),
	MARAUDER(184, Race.ERTHEIA, "Marauder", ERTHEIA_FIGHTER),
	CLOUD_BREAKER(185, Race.ERTHEIA, "Cloud Breaker", ERTHEIA_WIZARD),
	RIPPER(186, Race.ERTHEIA, "Ripper", MARAUDER),
	STRATOMANCER(187, Race.ERTHEIA, "Stratomancer", CLOUD_BREAKER),
	EVISCERATOR(188, Race.ERTHEIA, "Eviscerator", RIPPER),
	SAYHA_SEER(189, Race.ERTHEIA, "Sayha Seer", STRATOMANCER);

	private final int id;
	private final Race race;
	private final String name;
	private final ClassId parent;
	private EnumSet<ClassId> subclasses;

	/**
	 * Returns the level of the {@link ClassId}.
	 *
	 * @return int : The level (-1=dummy, 0=base, 1=1st class, 2=2nd class, 3=3rd class)
	 */
	public final int level() {
		return parent != null ? parent.level() + 1 : 0;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * @param classId The parent ClassId to check
	 * @return True if this Class is a child of the selected ClassId.
	 */
	public final boolean childOf(ClassId classId) {
		if (parent == null) {
			return false;
		}

		if (parent == classId) {
			return true;
		}

		return parent.childOf(classId);
	}

	/**
	 * @param classId the parent ClassId to check.
	 * @return true if this Class is equal to the selected ClassId or a child of the selected ClassId.
	 */
	public final boolean equalsOrChildOf(ClassId classId) {
		return this == classId || childOf(classId);
	}

	private void createSubclasses() {
		// only 2nd class level can have subclasses
		if (level() != 2) {
			subclasses = null;
			return;
		}

		subclasses = EnumSet.noneOf(ClassId.class);

		for (ClassId classId : values()) {
			// only second classes may be taken as subclass
			if (classId.level() != 2) {
				continue;
			}

			// Overlord, Warsmith or self class may never be taken as subclass
			if (classId == OVERLORD || classId == WARSMITH || classId == this) {
				continue;
			}

			// Elves may not sub Dark Elves and vice versa
			if ((race == Race.ELF && classId.race == Race.DARK_ELF) || (race == Race.DARK_ELF && classId.race == Race.ELF)) {
				continue;
			}

			subclasses.add(classId);
		}

		// remove class restricted classes
		switch (this) {
			case DARK_AVENGER:
			case PALADIN:
			case TEMPLE_KNIGHT:
			case SHILLIEN_KNIGHT:
				// remove restricted classes for tanks
				subclasses.removeAll(EnumSet.of(DARK_AVENGER, PALADIN, TEMPLE_KNIGHT, SHILLIEN_KNIGHT));
				break;

			case TREASURE_HUNTER:
			case ABYSS_WALKER:
			case PLAINS_WALKER:
				// remove restricted classes for assassins
				subclasses.removeAll(EnumSet.of(TREASURE_HUNTER, ABYSS_WALKER, PLAINS_WALKER));
				break;

			case HAWK_EYE:
			case SILVER_RANGER:
			case PHANTOM_RANGER:
				// remove restricted classes for archers
				subclasses.removeAll(EnumSet.of(HAWK_EYE, SILVER_RANGER, PHANTOM_RANGER));
				break;

			case WARLOCK:
			case ELEMENTAL_SUMMONER:
			case PHANTOM_SUMMONER:
				// remove restricted classes for summoners
				subclasses.removeAll(EnumSet.of(WARLOCK, ELEMENTAL_SUMMONER, PHANTOM_SUMMONER));
				break;

			case SORCERER:
			case SPELL_SINGER:
			case SPELL_HOWLER:
				// remove restricted classes for wizards
				subclasses.removeAll(EnumSet.of(SORCERER, SPELL_SINGER, SPELL_HOWLER));
				break;
		}
	}

	public static ClassId ofId(int id) {
		return Stream.of(values())
			.filter(e -> e.getId() == id)
			.findAny()
			.get();
	}

	static {
		// create subclass lists
		for (ClassId classId : values()) {
			classId.createSubclasses();
		}
	}
}
