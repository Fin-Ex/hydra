package ru.finex.ws.l2.model.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * This class defines all classes (ex : human fighter, darkFighter...) that a
 * player can chose.
 * <ul>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId or null if this class is the root</li>
 * </ul>
 */
@Slf4j
@Deprecated
public enum ClassId implements IdEnum {
	HUMAN_FIGHTER(Race.HUMAN, "Human Fighter", null),
	Warrior(Race.HUMAN, "Warrior", HUMAN_FIGHTER),
	Gladiator(Race.HUMAN, "Gladiator", Warrior),
	Warlord(Race.HUMAN, "Warlord", Warrior),
	Knight(Race.HUMAN, "Human Knight", HUMAN_FIGHTER),
	Paladin(Race.HUMAN, "Paladin", Knight),
	DarkAvenger(Race.HUMAN, "Dark Avenger", Knight),
	Rogue(Race.HUMAN, "Rogue", HUMAN_FIGHTER),
	TreasureHunter(Race.HUMAN, "Treasure Hunter", Rogue),
	Hawkeye(Race.HUMAN, "Hawkeye", Rogue),
	HUMAN_MYSTIC(Race.HUMAN, "Human Mystic", null),
	HumanWizard(Race.HUMAN, "Human Wizard", HUMAN_MYSTIC),
	Sorcerer(Race.HUMAN, "Sorcerer", HumanWizard),
	Necromancer(Race.HUMAN, "Necromancer", HumanWizard),
	Warlock(Race.HUMAN, "Warlock", HumanWizard),
	Cleric(Race.HUMAN, "Cleric", HUMAN_MYSTIC),
	Bishop(Race.HUMAN, "Bishop", Cleric),
	Prophet(Race.HUMAN, "Prophet", Cleric),
	ElvenFighter(Race.ELF, "Elven Fighter", null),
	ElvenKnight(Race.ELF, "Elven Knight", ElvenFighter),
	TempleKnight(Race.ELF, "Temple Knight", ElvenKnight),
	Swordsinger(Race.ELF, "Sword Singer", ElvenKnight),
	ElvenScout(Race.ELF, "Elven Scout", ElvenFighter),
	PlainsWalker(Race.ELF, "Plains Walker", ElvenScout),
	SilverRanger(Race.ELF, "Silver Ranger", ElvenScout),
	ElvenMystic(Race.ELF, "Elven Mystic", null),
	ElvenWizard(Race.ELF, "Elven Wizard", ElvenMystic),
	Spellsinger(Race.ELF, "Spellsinger", ElvenWizard),
	ElementalSummoner(Race.ELF, "Elemental Summoner", ElvenWizard),
	ElvenOracle(Race.ELF, "Elven Oracle", ElvenMystic),
	ElvenElder(Race.ELF, "Elven Elder", ElvenOracle),
	DarkFighter(Race.DARK_ELF, "Dark Fighter", null),
	PalusKnight(Race.DARK_ELF, "Palus Knight", DarkFighter),
	ShillienKnight(Race.DARK_ELF, "Shillien Knight", PalusKnight),
	Bladedancer(Race.DARK_ELF, "Bladedancer", PalusKnight),
	Assassin(Race.DARK_ELF, "Assassin", DarkFighter),
	AbyssWalker(Race.DARK_ELF, "Abyss Walker", Assassin),
	PhantomRanger(Race.DARK_ELF, "Phantom Ranger", Assassin),
	DarkMystic(Race.DARK_ELF, "Dark Mystic", null),
	DarkWizard(Race.DARK_ELF, "Dark Wizard", DarkMystic),
	Spellhowler(Race.DARK_ELF, "Spellhowler", DarkWizard),
	PhantomSummoner(Race.DARK_ELF, "Phantom Summoner", DarkWizard),
	ShillienOracle(Race.DARK_ELF, "Shillien Oracle", DarkMystic),
	ShillienElder(Race.DARK_ELF, "Shillien Elder", ShillienOracle),
	OrcFighter(Race.ORC, "Orc Fighter", null),
	OrcRaider(Race.ORC, "Orc Raider", OrcFighter),
	Destroyer(Race.ORC, "Destroyer", OrcRaider),
	Monk(Race.ORC, "Monk", OrcFighter),
	Tyrant(Race.ORC, "Tyrant", Monk),
	OrcMystic(Race.ORC, "Orc Mystic", null),
	OrcShaman(Race.ORC, "Orc Shaman", OrcMystic),
	Overlord(Race.ORC, "Overlord", OrcShaman),
	Warcryer(Race.ORC, "Warcryer", OrcShaman),
	DwarvenFighter(Race.DWARF, "Dwarven Fighter", null),
	Scavenger(Race.DWARF, "Scavenger", DwarvenFighter),
	BountyHunter(Race.DWARF, "Bounty Hunter", Scavenger),
	Artisan(Race.DWARF, "Artisan", DwarvenFighter),
	Warsmith(Race.DWARF, "Warsmith", Artisan),
	DUMMY_1(null, "dummy 1", null),
	DUMMY_2(null, "dummy 2", null),
	DUMMY_3(null, "dummy 3", null),
	DUMMY_4(null, "dummy 4", null),
	DUMMY_5(null, "dummy 5", null),
	DUMMY_6(null, "dummy 6", null),
	DUMMY_7(null, "dummy 7", null),
	DUMMY_8(null, "dummy 8", null),
	DUMMY_9(null, "dummy 9", null),
	DUMMY_10(null, "dummy 10", null),
	DUMMY_11(null, "dummy 11", null),
	DUMMY_12(null, "dummy 12", null),
	DUMMY_13(null, "dummy 13", null),
	DUMMY_14(null, "dummy 14", null),
	DUMMY_15(null, "dummy 15", null),
	DUMMY_16(null, "dummy 16", null),
	DUMMY_17(null, "dummy 17", null),
	DUMMY_18(null, "dummy 18", null),
	DUMMY_19(null, "dummy 19", null),
	DUMMY_20(null, "dummy 20", null),
	DUMMY_21(null, "dummy 21", null),
	DUMMY_22(null, "dummy 22", null),
	DUMMY_23(null, "dummy 23", null),
	DUMMY_24(null, "dummy 24", null),
	DUMMY_25(null, "dummy 25", null),
	DUMMY_26(null, "dummy 26", null),
	DUMMY_27(null, "dummy 27", null),
	DUMMY_28(null, "dummy 28", null),
	DUMMY_29(null, "dummy 29", null),
	DUMMY_30(null, "dummy 30", null),
	Duelist(Race.HUMAN, "Duelist", Gladiator),
	Dreadnought(Race.HUMAN, "Dreadnought", Warlord),
	PhoenixKnight(Race.HUMAN, "Phoenix Knight", Paladin),
	HellKnight(Race.HUMAN, "Hell Knight", DarkAvenger),
	Saggitarius(Race.HUMAN, "Sagittarius", Hawkeye),
	Adventurer(Race.HUMAN, "Adventurer", TreasureHunter),
	Archmage(Race.HUMAN, "Archmage", Sorcerer),
	Soultaker(Race.HUMAN, "Soultaker", Necromancer),
	ArcanaLord(Race.HUMAN, "Arcana Lord", Warlock),
	Cardinal(Race.HUMAN, "Cardinal", Bishop),
	Hierophant(Race.HUMAN, "Hierophant", Prophet),
	EvasTemplar(Race.ELF, "Eva's Templar", TempleKnight),
	SwordMuse(Race.ELF, "Sword Muse", Swordsinger),
	WindRider(Race.ELF, "Wind Rider", PlainsWalker),
	MoonlightSentinel(Race.ELF, "Moonlight Sentinel", SilverRanger),
	MysticMuse(Race.ELF, "Mystic Muse", Spellsinger),
	ElementalMaster(Race.ELF, "Elemental Master", ElementalSummoner),
	EvasSaint(Race.ELF, "Eva's Saint", ElvenElder),
	ShillienTemplar(Race.DARK_ELF, "Shillien Templar", ShillienKnight),
	SpectralDancer(Race.DARK_ELF, "Spectral Dancer", Bladedancer),
	GhostHunter(Race.DARK_ELF, "Ghost Hunter", AbyssWalker),
	GhostSentinel(Race.DARK_ELF, "Ghost Sentinel", PhantomRanger),
	StormScreamer(Race.DARK_ELF, "Storm Screamer", Spellhowler),
	SpectralMaster(Race.DARK_ELF, "Spectral Master", PhantomSummoner),
	ShillienSaint(Race.DARK_ELF, "Shillien Saint", ShillienElder),
	Titan(Race.ORC, "Titan", Destroyer),
	GrandGhavatari(Race.ORC, "Grand Khavatari", Tyrant),
	Dominator(Race.ORC, "Dominator", Overlord),
	Doomcryer(Race.ORC, "Doom Cryer", Warcryer),
	FortuneSeeker(Race.DWARF, "Fortune Seeker", BountyHunter),
	Maestro(Race.DWARF, "Maestro", Warsmith),

	WORLD_TRAP(null, null, null),
	PC_TRAP(null, null, null),
	DOPPELGANGER(null, null, null),
	SIEGE_ATTACKER(null, null, null),

	MALE_SOLDIER(Race.KAMAEL, "Soldier", null),
	FEMALE_SOLDIER(Race.KAMAEL, "Soldier", null),
	TROOPER(Race.KAMAEL, "Trooper", MALE_SOLDIER),
	WARDER(Race.KAMAEL, "Warden", FEMALE_SOLDIER),
	BERSERKER(Race.KAMAEL, "Berserker", TROOPER),
	MALE_SOULBREAKER(Race.KAMAEL, "Soulbreaker", TROOPER),
	FEMALE_SOULBREAKER(Race.KAMAEL, "Soulbreaker", WARDER),
	ARBALESTER(Race.KAMAEL, "Arbalester", WARDER),
	DOOMBRINGER(Race.KAMAEL, "Doombringer", BERSERKER),
	MALE_SOUL_HOUND(Race.KAMAEL, "Soul Hound", MALE_SOULBREAKER),
	FEMALE_SOUL_HOUND(Race.KAMAEL, "Soul Hound", FEMALE_SOULBREAKER),
	TRICKSTER(Race.KAMAEL, "Trickster", ARBALESTER),
	INSPECTOR(Race.KAMAEL, "Inspector", WARDER),
	JUDICATOR(Race.KAMAEL, "Judicator", INSPECTOR),
	// skipped: 5th class levels
	ERTHEIA_FIGHTER(182, Race.ERTHEIA, "Fighter", null),
	ERTHEIA_WIZARD(183, Race.ERTHEIA, "Wizard", null),
	MARAUDER(184, Race.ERTHEIA, "Marauder", ERTHEIA_FIGHTER),
	CLOUD_BREAKER(185, Race.ERTHEIA, "Cloud Breaker", ERTHEIA_WIZARD),
	RIPPER(186, Race.ERTHEIA, "Ripper", MARAUDER),
	STRATOMANCER(187, Race.ERTHEIA, "Stratomancer", CLOUD_BREAKER),
	EVISCERATOR(188, Race.ERTHEIA, "Eviscerator", RIPPER),
	SAYHA_SEER(189, Race.ERTHEIA, "Sayha Seer", STRATOMANCER);

	/**
	 * The ID of the class
	 */
	@Getter
	private final int id;

	/**
	 * The ClassRace object of the class
	 */
	@Getter
	private final Race race;

	/**
	 * The name of the class
	 */
	@Getter
	private final String name;

	/**
	 * The parent ClassId of the class
	 */
	@Getter
	private final ClassId parent;

	/**
	 * The set of subclasses available for the class
	 */
	private EnumSet<ClassId> subclasses;

	ClassId(int id, Race race, String name, ClassId parent) {
		this.id = id;
		this.race = race;
		this.name = name;
		this.parent = parent;
	}

	ClassId(Race race, String name, ClassId parent) {
		this.id = ordinal();
		this.race = race;
		this.name = name;
		this.parent = parent;
	}

	/**
	 * Returns the level of the {@link ClassId}.
	 *
	 * @return int : The level (-1=dummy, 0=base, 1=1st class, 2=2nd class,
	 * 3=3rd class)
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
	 * @return true if this Class is equal to the selected ClassId or a child of
	 * the selected ClassId.
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
			if (classId == Overlord || classId == Warsmith || classId == this) {
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
			case DarkAvenger:
			case Paladin:
			case TempleKnight:
			case ShillienKnight:
				// remove restricted classes for tanks
				subclasses.removeAll(EnumSet.of(DarkAvenger, Paladin, TempleKnight, ShillienKnight));
				break;

			case TreasureHunter:
			case AbyssWalker:
			case PlainsWalker:
				// remove restricted classes for assassins
				subclasses.removeAll(EnumSet.of(TreasureHunter, AbyssWalker, PlainsWalker));
				break;

			case Hawkeye:
			case SilverRanger:
			case PhantomRanger:
				// remove restricted classes for archers
				subclasses.removeAll(EnumSet.of(Hawkeye, SilverRanger, PhantomRanger));
				break;

			case Warlock:
			case ElementalSummoner:
			case PhantomSummoner:
				// remove restricted classes for summoners
				subclasses.removeAll(EnumSet.of(Warlock, ElementalSummoner, PhantomSummoner));
				break;

			case Sorcerer:
			case Spellsinger:
			case Spellhowler:
				// remove restricted classes for wizards
				subclasses.removeAll(EnumSet.of(Sorcerer, Spellsinger, Spellhowler));
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
