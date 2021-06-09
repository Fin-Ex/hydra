package sf.l2j.gameserver.model.base;

import lombok.extern.slf4j.Slf4j;
import ru.finex.gs.model.ClassType;
import ru.finex.gs.model.PlayerRace;

import java.util.EnumSet;

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
public enum ClassId {
	HumanFighter(PlayerRace.HUMAN, ClassType.FIGHTER, 0, "Human Fighter", null),
	Warrior(PlayerRace.HUMAN, ClassType.FIGHTER, 1, "Warrior", HumanFighter),
	Gladiator(PlayerRace.HUMAN, ClassType.FIGHTER, 2, "Gladiator", Warrior),
	Warlord(PlayerRace.HUMAN, ClassType.FIGHTER, 2, "Warlord", Warrior),
	Knight(PlayerRace.HUMAN, ClassType.FIGHTER, 1, "Human Knight", HumanFighter),
	Paladin(PlayerRace.HUMAN, ClassType.FIGHTER, 2, "Paladin", Knight),
	DarkAvenger(PlayerRace.HUMAN, ClassType.FIGHTER, 2, "Dark Avenger", Knight),
	Rogue(PlayerRace.HUMAN, ClassType.FIGHTER, 1, "Rogue", HumanFighter),
	TreasureHunter(PlayerRace.HUMAN, ClassType.FIGHTER, 2, "Treasure Hunter", Rogue),
	Hawkeye(PlayerRace.HUMAN, ClassType.FIGHTER, 2, "Hawkeye", Rogue),
	HymanMystic(PlayerRace.HUMAN, ClassType.MYSTIC, 0, "Human Mystic", null),
	HumanWizard(PlayerRace.HUMAN, ClassType.MYSTIC, 1, "Human Wizard", HymanMystic),
	Sorcerer(PlayerRace.HUMAN, ClassType.MYSTIC, 2, "Sorcerer", HumanWizard),
	Necromancer(PlayerRace.HUMAN, ClassType.MYSTIC, 2, "Necromancer", HumanWizard),
	Warlock(PlayerRace.HUMAN, ClassType.MYSTIC, 2, "Warlock", HumanWizard),
	Cleric(PlayerRace.HUMAN, ClassType.PRIEST, 1, "Cleric", HymanMystic),
	Bishop(PlayerRace.HUMAN, ClassType.PRIEST, 2, "Bishop", Cleric),
	Prophet(PlayerRace.HUMAN, ClassType.PRIEST, 2, "Prophet", Cleric),
	ElvenFighter(PlayerRace.ELF, ClassType.FIGHTER, 0, "Elven Fighter", null),
	ElvenKnight(PlayerRace.ELF, ClassType.FIGHTER, 1, "Elven Knight", ElvenFighter),
	TempleKnight(PlayerRace.ELF, ClassType.FIGHTER, 2, "Temple Knight", ElvenKnight),
	Swordsinger(PlayerRace.ELF, ClassType.FIGHTER, 2, "Sword Singer", ElvenKnight),
	ElvenScout(PlayerRace.ELF, ClassType.FIGHTER, 1, "Elven Scout", ElvenFighter),
	PlainsWalker(PlayerRace.ELF, ClassType.FIGHTER, 2, "Plains Walker", ElvenScout),
	SilverRanger(PlayerRace.ELF, ClassType.FIGHTER, 2, "Silver Ranger", ElvenScout),
	ElvenMystic(PlayerRace.ELF, ClassType.MYSTIC, 0, "Elven Mystic", null),
	ElvenWizard(PlayerRace.ELF, ClassType.MYSTIC, 1, "Elven Wizard", ElvenMystic),
	Spellsinger(PlayerRace.ELF, ClassType.MYSTIC, 2, "Spellsinger", ElvenWizard),
	ElementalSummoner(PlayerRace.ELF, ClassType.MYSTIC, 2, "Elemental Summoner", ElvenWizard),
	ElvenOracle(PlayerRace.ELF, ClassType.PRIEST, 1, "Elven Oracle", ElvenMystic),
	ElvenElder(PlayerRace.ELF, ClassType.PRIEST, 2, "Elven Elder", ElvenOracle),
	DarkFighter(PlayerRace.DARK_ELF, ClassType.FIGHTER, 0, "Dark Fighter", null),
	PalusKnight(PlayerRace.DARK_ELF, ClassType.FIGHTER, 1, "Palus Knight", DarkFighter),
	ShillienKnight(PlayerRace.DARK_ELF, ClassType.FIGHTER, 2, "Shillien Knight", PalusKnight),
	Bladedancer(PlayerRace.DARK_ELF, ClassType.FIGHTER, 2, "Bladedancer", PalusKnight),
	Assassin(PlayerRace.DARK_ELF, ClassType.FIGHTER, 1, "Assassin", DarkFighter),
	AbyssWalker(PlayerRace.DARK_ELF, ClassType.FIGHTER, 2, "Abyss Walker", Assassin),
	PhantomRanger(PlayerRace.DARK_ELF, ClassType.FIGHTER, 2, "Phantom Ranger", Assassin),
	DarkMystic(PlayerRace.DARK_ELF, ClassType.MYSTIC, 0, "Dark Mystic", null),
	DarkWizard(PlayerRace.DARK_ELF, ClassType.MYSTIC, 1, "Dark Wizard", DarkMystic),
	Spellhowler(PlayerRace.DARK_ELF, ClassType.MYSTIC, 2, "Spellhowler", DarkWizard),
	PhantomSummoner(PlayerRace.DARK_ELF, ClassType.MYSTIC, 2, "Phantom Summoner", DarkWizard),
	ShillienOracle(PlayerRace.DARK_ELF, ClassType.PRIEST, 1, "Shillien Oracle", DarkMystic),
	ShillienElder(PlayerRace.DARK_ELF, ClassType.PRIEST, 2, "Shillien Elder", ShillienOracle),
	OrcFighter(PlayerRace.ORC, ClassType.FIGHTER, 0, "Orc Fighter", null),
	OrcRaider(PlayerRace.ORC, ClassType.FIGHTER, 1, "Orc Raider", OrcFighter),
	Destroyer(PlayerRace.ORC, ClassType.FIGHTER, 2, "Destroyer", OrcRaider),
	Monk(PlayerRace.ORC, ClassType.FIGHTER, 1, "Monk", OrcFighter),
	Tyrant(PlayerRace.ORC, ClassType.FIGHTER, 2, "Tyrant", Monk),
	OrcMystic(PlayerRace.ORC, ClassType.MYSTIC, 0, "Orc Mystic", null),
	OrcShaman(PlayerRace.ORC, ClassType.MYSTIC, 1, "Orc Shaman", OrcMystic),
	Overlord(PlayerRace.ORC, ClassType.MYSTIC, 2, "Overlord", OrcShaman),
	Warcryer(PlayerRace.ORC, ClassType.MYSTIC, 2, "Warcryer", OrcShaman),
	DwarvenFighter(PlayerRace.DWARF, ClassType.FIGHTER, 0, "Dwarven Fighter", null),
	Scavenger(PlayerRace.DWARF, ClassType.FIGHTER, 1, "Scavenger", DwarvenFighter),
	BountyHunter(PlayerRace.DWARF, ClassType.FIGHTER, 2, "Bounty Hunter", Scavenger),
	Artisan(PlayerRace.DWARF, ClassType.FIGHTER, 1, "Artisan", DwarvenFighter),
	Warsmith(PlayerRace.DWARF, ClassType.FIGHTER, 2, "Warsmith", Artisan),
	DUMMY_1(null, null, -1, "dummy 1", null),
	DUMMY_2(null, null, -1, "dummy 2", null),
	DUMMY_3(null, null, -1, "dummy 3", null),
	DUMMY_4(null, null, -1, "dummy 4", null),
	DUMMY_5(null, null, -1, "dummy 5", null),
	DUMMY_6(null, null, -1, "dummy 6", null),
	DUMMY_7(null, null, -1, "dummy 7", null),
	DUMMY_8(null, null, -1, "dummy 8", null),
	DUMMY_9(null, null, -1, "dummy 9", null),
	DUMMY_10(null, null, -1, "dummy 10", null),
	DUMMY_11(null, null, -1, "dummy 11", null),
	DUMMY_12(null, null, -1, "dummy 12", null),
	DUMMY_13(null, null, -1, "dummy 13", null),
	DUMMY_14(null, null, -1, "dummy 14", null),
	DUMMY_15(null, null, -1, "dummy 15", null),
	DUMMY_16(null, null, -1, "dummy 16", null),
	DUMMY_17(null, null, -1, "dummy 17", null),
	DUMMY_18(null, null, -1, "dummy 18", null),
	DUMMY_19(null, null, -1, "dummy 19", null),
	DUMMY_20(null, null, -1, "dummy 20", null),
	DUMMY_21(null, null, -1, "dummy 21", null),
	DUMMY_22(null, null, -1, "dummy 22", null),
	DUMMY_23(null, null, -1, "dummy 23", null),
	DUMMY_24(null, null, -1, "dummy 24", null),
	DUMMY_25(null, null, -1, "dummy 25", null),
	DUMMY_26(null, null, -1, "dummy 26", null),
	DUMMY_27(null, null, -1, "dummy 27", null),
	DUMMY_28(null, null, -1, "dummy 28", null),
	DUMMY_29(null, null, -1, "dummy 29", null),
	DUMMY_30(null, null, -1, "dummy 30", null),
	Duelist(PlayerRace.HUMAN, ClassType.FIGHTER, 3, "Duelist", Gladiator),
	Dreadnought(PlayerRace.HUMAN, ClassType.FIGHTER, 3, "Dreadnought", Warlord),
	PhoenixKnight(PlayerRace.HUMAN, ClassType.FIGHTER, 3, "Phoenix Knight", Paladin),
	HellKnight(PlayerRace.HUMAN, ClassType.FIGHTER, 3, "Hell Knight", DarkAvenger),
	Saggitarius(PlayerRace.HUMAN, ClassType.FIGHTER, 3, "Sagittarius", Hawkeye),
	Adventurer(PlayerRace.HUMAN, ClassType.FIGHTER, 3, "Adventurer", TreasureHunter),
	Archmage(PlayerRace.HUMAN, ClassType.MYSTIC, 3, "Archmage", Sorcerer),
	Soultaker(PlayerRace.HUMAN, ClassType.MYSTIC, 3, "Soultaker", Necromancer),
	ArcanaLord(PlayerRace.HUMAN, ClassType.MYSTIC, 3, "Arcana Lord", Warlock),
	Cardinal(PlayerRace.HUMAN, ClassType.PRIEST, 3, "Cardinal", Bishop),
	Hierophant(PlayerRace.HUMAN, ClassType.PRIEST, 3, "Hierophant", Prophet),
	EvasTemplar(PlayerRace.ELF, ClassType.FIGHTER, 3, "Eva's Templar", TempleKnight),
	SwordMuse(PlayerRace.ELF, ClassType.FIGHTER, 3, "Sword Muse", Swordsinger),
	WindRider(PlayerRace.ELF, ClassType.FIGHTER, 3, "Wind Rider", PlainsWalker),
	MoonlightSentinel(PlayerRace.ELF, ClassType.FIGHTER, 3, "Moonlight Sentinel", SilverRanger),
	MysticMuse(PlayerRace.ELF, ClassType.MYSTIC, 3, "Mystic Muse", Spellsinger),
	ElementalMaster(PlayerRace.ELF, ClassType.MYSTIC, 3, "Elemental Master", ElementalSummoner),
	EvasSaint(PlayerRace.ELF, ClassType.PRIEST, 3, "Eva's Saint", ElvenElder),
	ShillienTemplar(PlayerRace.DARK_ELF, ClassType.FIGHTER, 3, "Shillien Templar", ShillienKnight),
	SpectralDancer(PlayerRace.DARK_ELF, ClassType.FIGHTER, 3, "Spectral Dancer", Bladedancer),
	GhostHunter(PlayerRace.DARK_ELF, ClassType.FIGHTER, 3, "Ghost Hunter", AbyssWalker),
	GhostSentinel(PlayerRace.DARK_ELF, ClassType.FIGHTER, 3, "Ghost Sentinel", PhantomRanger),
	StormScreamer(PlayerRace.DARK_ELF, ClassType.MYSTIC, 3, "Storm Screamer", Spellhowler),
	SpectralMaster(PlayerRace.DARK_ELF, ClassType.MYSTIC, 3, "Spectral Master", PhantomSummoner),
	ShillienSaint(PlayerRace.DARK_ELF, ClassType.PRIEST, 3, "Shillien Saint", ShillienElder),
	Titan(PlayerRace.ORC, ClassType.FIGHTER, 3, "Titan", Destroyer),
	GrandGhavatari(PlayerRace.ORC, ClassType.FIGHTER, 3, "Grand Khavatari", Tyrant),
	Dominator(PlayerRace.ORC, ClassType.MYSTIC, 3, "Dominator", Overlord),
	Doomcryer(PlayerRace.ORC, ClassType.MYSTIC, 3, "Doom Cryer", Warcryer),
	FortuneSeeker(PlayerRace.DWARF, ClassType.FIGHTER, 3, "Fortune Seeker", BountyHunter),
	Maestro(PlayerRace.DWARF, ClassType.FIGHTER, 3, "Maestro", Warsmith);

	public static final ClassId[] VALUES = values();

	/**
	 * The ID of the class
	 */
	private final int _id;

	/**
	 * The ClassRace object of the class
	 */
	private final PlayerRace _race;

	/**
	 * The ClassType of the class
	 */
	private final ClassType _type;

	/**
	 * The level of the class
	 */
	private final int _level;

	/**
	 * The name of the class
	 */
	private final String _name;

	/**
	 * The parent ClassId of the class
	 */
	private final ClassId _parent;

	/**
	 * The set of subclasses available for the class
	 */
	private EnumSet<ClassId> _subclasses;

	private ClassId(PlayerRace race, ClassType type, int level, String name, ClassId parent) {
		_id = ordinal();
		_race = race;
		_type = type;
		_level = level;
		_name = name;
		_parent = parent;
	}

	/**
	 * Returns the ID of the {@link ClassId}.
	 *
	 * @return int : The ID.
	 */
	public final int getId() {
		return _id;
	}

	/**
	 * Returns the {@link PlayerRace} of the {@link ClassId}.
	 *
	 * @return {@link PlayerRace} : The race.
	 */
	public final PlayerRace getRace() {
		return _race;
	}

	/**
	 * Returns the {@link ClassType} of the {@link ClassId}.
	 *
	 * @return {@link ClassType} : The type.
	 */
	public final ClassType getType() {
		return _type;
	}

	/**
	 * Returns the level of the {@link ClassId}.
	 *
	 * @return int : The level (-1=dummy, 0=base, 1=1st class, 2=2nd class,
	 * 3=3rd class)
	 */
	public final int level() {
		return _level;
	}

	@Override
	public String toString() {
		return _name;
	}

	/**
	 * Returns the parent {@link ClassId} of the {@link ClassId}.
	 *
	 * @return {@link ClassId} : The parent.
	 */
	public final ClassId getParent() {
		return _parent;
	}

	/**
	 * @param classId The parent ClassId to check
	 * @return True if this Class is a child of the selected ClassId.
	 */
	public final boolean childOf(ClassId classId) {
		if (_parent == null) {
			return false;
		}

		if (_parent == classId) {
			return true;
		}

		return _parent.childOf(classId);
	}

	/**
	 * @param classId the parent ClassId to check.
	 * @return true if this Class is equal to the selected ClassId or a child of
	 * the selected ClassId.
	 */
	public final boolean equalsOrChildOf(ClassId classId) {
		return this == classId || childOf(classId);
	}

	private final void createSubclasses() {
		// only 2nd class level can have subclasses
		if (_level != 2) {
			_subclasses = null;
			return;
		}

		_subclasses = EnumSet.noneOf(ClassId.class);

		for (ClassId classId : VALUES) {
			// only second classes may be taken as subclass
			if (classId._level != 2) {
				continue;
			}

			// Overlord, Warsmith or self class may never be taken as subclass
			if (classId == Overlord || classId == Warsmith || classId == this) {
				continue;
			}

			// Elves may not sub Dark Elves and vice versa
			if ((_race == PlayerRace.ELF && classId._race == PlayerRace.DARK_ELF) || (_race == PlayerRace.DARK_ELF && classId._race == PlayerRace.ELF)) {
				continue;
			}

			_subclasses.add(classId);
		}

		// remove class restricted classes
		switch (this) {
			case DarkAvenger:
			case Paladin:
			case TempleKnight:
			case ShillienKnight:
				// remove restricted classes for tanks
				_subclasses.removeAll(EnumSet.of(DarkAvenger, Paladin, TempleKnight, ShillienKnight));
				break;

			case TreasureHunter:
			case AbyssWalker:
			case PlainsWalker:
				// remove restricted classes for assassins
				_subclasses.removeAll(EnumSet.of(TreasureHunter, AbyssWalker, PlainsWalker));
				break;

			case Hawkeye:
			case SilverRanger:
			case PhantomRanger:
				// remove restricted classes for archers
				_subclasses.removeAll(EnumSet.of(Hawkeye, SilverRanger, PhantomRanger));
				break;

			case Warlock:
			case ElementalSummoner:
			case PhantomSummoner:
				// remove restricted classes for summoners
				_subclasses.removeAll(EnumSet.of(Warlock, ElementalSummoner, PhantomSummoner));
				break;

			case Sorcerer:
			case Spellsinger:
			case Spellhowler:
				// remove restricted classes for wizards
				_subclasses.removeAll(EnumSet.of(Sorcerer, Spellsinger, Spellhowler));
				break;
		}
	}

	static {
		// create subclass lists
		for (ClassId classId : VALUES) {
			classId.createSubclasses();
		}
	}
}
