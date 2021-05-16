package sf.l2j.gameserver.skills;

import java.util.NoSuchElementException;

/**
 * Enum of basic stats.
 *
 * @author mkizub
 */
public enum Stats {
	// HP & MP
	MaxHP,
	MaxMP,
	MaxCP,
	RegenHP,
	RegenCP,
	RegenMP,
	GainMP,
	GainHP,
	GiveHP,
	GiveMP,
	HpRegenInterval,
	MpRegenInterval,
	CpRegenInterval,
	// Atk & Def
	PDef,
	MDef,
	PAtk,
	MAtk,
	PAtkSpd,
	MAtkSpd,
	SpellReuse,
	SkillReuse,
	ShieldDefense,
	BlockAngle,
	BlockRate,
	AttackParry,
	SkillParry,
	SpellParry,
	ArrowReloadSpd,
	CriticalDamage,
	CriticalDamageAdd,
	PvpPhysDmg,
	PvpMagicalDmg,
	PvpSkillDmg,
	PvpSkillDef,
	// Atk & Def rates
	Evasion,
	SkillDodge,
	SpellDodge,
	CriticalRate,
	BlowRate,
	LethalRate,
	MagicCriticalRate,
	AttackCancel,
	// Accuracy and range
	Accuracy,
	PAtkRange,
	PAtkAngle,
	AtkCountMax,
	PoleAtkPercent,
	// Run speed
	Speed,
	// Main params
	STR,
	CON,
	DEX,
	INT,
	WIT,
	MEN,
	// stats of various abilities
	Breath,
	Fall,
	// Status attacks - increase status attacks success chance/time
	Aggression,
	Bleed,
	Poison,
	@Deprecated
	Stun,
	@Deprecated
	Root,
	@Deprecated
	Movement,
	@Deprecated
	Confusion,
	@Deprecated
	Sleep,
	Valakas,
	ValakasRes,
	// Elemental resistances/vulnerabilities
	FireRes,
	WaterRes,
	WindRes,
	EarthRes,
	HolyRes,
	DarkRes,
	// Elemental power (used for skills such as Holy blade)
	FirePower,
	WaterPower,
	WindPower,
	EarthPower,
	HolyPower,
	DarkPower,
	// Weapons vuln
	SwordWpnVuln,
	BluntWpnVuln,
	DaggerWpnVuln,
	BowWpnVuln,
	PoleWpnVuln,
	DualWpnVuln,
	DualFistWpnVuln,
	BigSwordWpnVuln,
	BigBluntWpnVuln,
	ReflectDamPercent,
	ReflectSpellEffect,
	ReflectSkillEffect,
	VengeanceMDam,
	VengeancePDam,
	Vampirism,
	TransferDam,
	PAtkPlants,
	PAtkInsects,
	PAtkAnimals,
	PAtkDemons,
	PAtkMonsters,
	PAtkDragons,
	PAtkGiants,
	PAtkMagicCreatures,
	PAtkUndeads,
	PAtkAngels,
	PAtkSpirits,
	PAtkFairies,
	PDefPlants,
	PDefInsects,
	PDefAnimals,
	PDefDemons,
	PDefMonsters,
	PDefDragons,
	PDefGiants,
	PDefMagicCreatures,
	PDefUndeads,
	PDefAngels,
	PDefSpirits,
	PDefFairies,
	// ExSkill :)
	MaxLoad,
	InventoryLimit,
	WHLimit,
	FreightLimit,
	PrivateSellLimit,
	PrivateBuyLimit,
	DwarfRecipeLimit,
	CommonRecipeLimit,
	// C4 Stats
	PhysicalMpConsumeRate,
	MagicalMpConsumeRate,
	DanceMpConsumeRate,
	// Skill mastery
	SkillMastery,
	// Special Stats
	AttackMpConsumeRate,
	AttackHpConsumeRate,
	MagicalDamage,
	PhysicalDamage,
	SpellPower,
	SkillPower,
	AoEDamage,
	DamageAbsorbRate,
	SkillAbsorbRate,
	SpellAbsorb,
	// Vulnerability - decrease the success rate of apply
	StunVuln,
	ParalyzeVuln,
	StoneVuln,
	RootVuln,
	FearVuln,
	MuteVuln,
	DebuffVuln,
	DerangementVuln,
	SleepVuln,
	BleedVuln,
	PoisonVuln,
	// Defense - decrease effect time
	StunDef,
	ParalyzeDef,
	StoneDef,
	RootDef,
	FearDef,
	MuteDef,
	DebuffDef,
	DerangementDef,
	SleepDef,
	BleedDef,
	PoisonDef,
	// Vulnerabilities (old for Interlude)
	CriticalVuln, // decrease takened damage from crit attacks
	DamageZoneVuln, // decrease damage incoming from damage zones
	CancelVuln, // ???

	Resilence,;

	public static final int NUM_STATS = values().length;

	public static Stats valueOfXml(String name) {
		name = name.intern();
		for (Stats stat : values()) {
			if (stat.name().equalsIgnoreCase(name)) {
				return stat;
			}
		}

		throw new NoSuchElementException("Unknown name '" + name + "' for enum BaseStats");
	}
}
