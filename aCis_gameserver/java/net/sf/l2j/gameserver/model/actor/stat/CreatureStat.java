package net.sf.l2j.gameserver.model.actor.stat;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.Calculator;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;

public class CreatureStat {

	private final Creature _activeChar;

	private long _exp = 0;
	private int _sp = 0;
	private byte _level = 1;

	public CreatureStat(Creature activeChar) {
		_activeChar = activeChar;
	}

	/**
	 * Calculate the new value of the state with modifiers that will be applied
	 * on the targeted Creature.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * A Creature owns a table of Calculators called <B>_calculators</B>. Each
	 * Calculator (a calculator per state) own a table of Func object. A Func
	 * object is a mathematic function that permit to calculate the modifier of
	 * a state (ex : REGENERATE_HP_RATE...) : <BR>
	 * <BR>
	 * FuncAtkAccuracy -> Math.sqrt(_player.getDEX())*6+_player.getLevel()<BR>
	 * <BR>
	 * When the calc method of a calculator is launched, each mathematic
	 * function is called according to its priority <B>_order</B>. Indeed, Func
	 * with lowest priority order is executed firsta and Funcs with the same
	 * order are executed in unspecified order. The result of the calculation is
	 * stored in the value property of an Env class instance.<BR>
	 * <BR>
	 *
	 * @param stat The stat to calculate the new value with modifiers
	 * @param init The initial value of the stat before applying modifiers
	 * @param target The L2Charcater whose properties will be used in the
	 * calculation (ex : CON, INT...)
	 * @param skill The L2Skill whose properties will be used in the calculation
	 * (ex : Level...)
	 * @return
	 */
	public final double calcStat(Stats stat, double init, Creature target, L2Skill skill) {
		if (_activeChar == null || stat == null) {
			return init;
		}

		final int id = stat.ordinal();

		final Calculator c = _activeChar.getCalculators()[id];
		if (c == null || c.size() == 0) {
			return init;
		}

		// Create and init an Env object to pass parameters to the Calculator
		final Env env = new Env();
		env.setCharacter(_activeChar);
		env.setTarget(target);
		env.setSkill(skill);
		env.setValue(init);

		// Launch the calculation
		c.calc(env);

		// avoid some troubles with negative stats (some stats should never be negative)
		if (env.getValue() <= 0) {
			switch (stat) {
				case MaxHP:
				case MaxMP:
				case MaxCP:
				case MDef:
				case PDef:
				case PAtk:
				case MAtk:
				case PAtkSpd:
				case MAtkSpd:
				case ShieldDefense:
				case CON:
				case DEX:
				case INT:
				case MEN:
				case STR:
				case WIT:
					env.setValue(1);
			}
		}
		return env.getValue();
	}

	/**
	 * @return the STR of the Creature (base+modifier).
	 */
	public final int getSTR() {
		return (int) calcStat(Stats.STR, _activeChar.getTemplate().getBaseSTR(), null, null);
	}

	/**
	 * @return the DEX of the Creature (base+modifier).
	 */
	public final int getDEX() {
		return (int) calcStat(Stats.DEX, _activeChar.getTemplate().getBaseDEX(), null, null);
	}

	/**
	 * @return the CON of the Creature (base+modifier).
	 */
	public final int getCON() {
		return (int) calcStat(Stats.CON, _activeChar.getTemplate().getBaseCON(), null, null);
	}

	/**
	 * @return the INT of the Creature (base+modifier).
	 */
	public int getINT() {
		return (int) calcStat(Stats.INT, _activeChar.getTemplate().getBaseINT(), null, null);
	}

	/**
	 * @return the MEN of the Creature (base+modifier).
	 */
	public final int getMEN() {
		return (int) calcStat(Stats.MEN, _activeChar.getTemplate().getBaseMEN(), null, null);
	}

	/**
	 * @return the WIT of the Creature (base+modifier).
	 */
	public final int getWIT() {
		return (int) calcStat(Stats.WIT, _activeChar.getTemplate().getBaseWIT(), null, null);
	}

	/**
	 * @param target
	 * @param skill
	 * @return the Critical Hit rate (base+modifier) of the Creature.
	 */
	public int getCriticalHit(Creature target, L2Skill skill) {
		int value = _activeChar.getTemplate().getBaseCritRate();
		if (target != null) {
			value = (int) target.calcStat(Stats.Resilence, value, null, skill);
		}
		return Math.min((int) calcStat(Stats.CriticalRate, value, target, skill), 500);
	}

	/**
	 * @param target
	 * @param skill
	 * @return the Magic Critical Hit rate (base+modifier) of the Creature.
	 */
	public final int getMCriticalHit(Creature target, L2Skill skill) {
		return (int) calcStat(Stats.MagicCriticalRate, 8, target, skill);
	}

	/**
	 * @param target
	 * @return the Attack Evasion rate (base+modifier) of the Creature.
	 */
	public int getEvasionRate(Creature target) {
		return (int) calcStat(Stats.Evasion, 0, target, null);
	}

	/**
	 * @return the Accuracy (base+modifier) of the Creature in function of the
	 * Weapon Expertise Penalty.
	 */
	public int getAccuracy() {
		return (int) calcStat(Stats.Accuracy, 0, null, null);
	}

	public int getMaxHp() {
		return (int) calcStat(Stats.MaxHP, _activeChar.getTemplate().getBaseHpMax(_activeChar.getLevel()), null, null);
	}

	public int getMaxCp() {
		return 0;
	}

	public int getMaxMp() {
		return (int) calcStat(Stats.MaxMP, _activeChar.getTemplate().getBaseMpMax(_activeChar.getLevel()), null, null);
	}

	/**
	 * @param target The Creature targeted by the skill
	 * @param skill The L2Skill used against the target
	 * @return the MAtk (base+modifier) of the Creature for a skill used in
	 * function of abnormal effects in progress.
	 */
	public int getMAtk(Creature target, L2Skill skill) {
		double attack = _activeChar.getTemplate().getBaseMAtk() * ((_activeChar.isChampion()) ? Config.CHAMPION_ATK : 1);

		// Add the power of the skill to the attack effect
		if (skill != null) {
			attack += calcStat(Stats.SpellPower, skill.getPower(), target, skill);
		}

		// Calculate modifiers Magic Attack
		return (int) calcStat(Stats.MAtk, attack, target, skill);
	}

	/**
	 * @return the MAtk Speed (base+modifier) of the Creature in function of the
	 * Armour Expertise Penalty.
	 */
	public int getMAtkSpd() {
		return (int) calcStat(Stats.MAtkSpd, 333.0 * ((_activeChar.isChampion()) ? Config.CHAMPION_SPD_ATK : 1), null, null);
	}

	/**
	 * @param target The Creature targeted by the skill
	 * @param skill The L2Skill used against the target
	 * @return the MDef (base+modifier) of the Creature against a skill in
	 * function of abnormal effects in progress.
	 */
	public int getMDef(Creature target, L2Skill skill) {
		// Calculate modifiers Magic Attack
		return (int) calcStat(Stats.MDef, _activeChar.getTemplate().getBaseMDef() * ((_activeChar.isRaid()) ? Config.RAID_DEFENCE_MULTIPLIER : 1), target, skill);
	}

	/**
	 * @param target
	 * @return the PAtk (base+modifier) of the Creature.
	 */
	public int getPAtk(Creature target) {
		return (int) calcStat(Stats.PAtk, _activeChar.getTemplate().getBasePAtk() * ((_activeChar.isChampion()) ? Config.CHAMPION_ATK : 1), target, null);
	}

	/**
	 * @return the PAtk Speed (base+modifier) of the Creature in function of the
	 * Armour Expertise Penalty.
	 */
	public int getPAtkSpd() {
		return (int) calcStat(Stats.PAtkSpd, _activeChar.getTemplate().getBasePAtkSpd() * ((_activeChar.isChampion()) ? Config.CHAMPION_SPD_ATK : 1), null, null);
	}

	/**
	 * @param target
	 * @return the PDef (base+modifier) of the Creature.
	 */
	public int getPDef(Creature target) {
		return (int) calcStat(Stats.PDef, _activeChar.getTemplate().getBasePDef() * ((_activeChar.isRaid()) ? Config.RAID_DEFENCE_MULTIPLIER : 1), target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against animals.
	 */
	public final double getPAtkAnimals(Creature target) {
		return calcStat(Stats.PAtkAnimals, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against dragons.
	 */
	public final double getPAtkDragons(Creature target) {
		return calcStat(Stats.PAtkDragons, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against insects.
	 */
	public final double getPAtkInsects(Creature target) {
		return calcStat(Stats.PAtkInsects, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against monsters.
	 */
	public final double getPAtkMonsters(Creature target) {
		return calcStat(Stats.PAtkMonsters, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against undeads.
	 */
	public final double getPAtkUndeads(Creature target) {
		return calcStat(Stats.PAtkUndeads, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against plants.
	 */
	public final double getPAtkPlants(Creature target) {
		return calcStat(Stats.PAtkPlants, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against giants.
	 */
	public final double getPAtkGiants(Creature target) {
		return calcStat(Stats.PAtkGiants, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against magic creatures
	 */
	public final double getPAtkMagicCreatures(Creature target) {
		return calcStat(Stats.PAtkMagicCreatures, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PAtk Modifier against demons
	 */
	public final double getPAtkDemons(Creature target) {
		return calcStat(Stats.PAtkDemons, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against animals.
	 */
	public final double getPDefAnimals(Creature target) {
		return calcStat(Stats.PDefAnimals, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against dragons.
	 */
	public final double getPDefDragons(Creature target) {
		return calcStat(Stats.PDefDragons, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against insects.
	 */
	public final double getPDefInsects(Creature target) {
		return calcStat(Stats.PDefInsects, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against monsters.
	 */
	public final double getPDefMonsters(Creature target) {
		return calcStat(Stats.PDefMonsters, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against plants.
	 */
	public final double getPDefPlants(Creature target) {
		return calcStat(Stats.PDefPlants, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against giants.
	 */
	public final double getPDefGiants(Creature target) {
		return calcStat(Stats.PDefGiants, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against magic creature.
	 */
	public final double getPDefMagicCreatures(Creature target) {
		return calcStat(Stats.PDefMagicCreatures, 1, target, null);
	}

	/**
	 * @param target
	 * @return the PDef Modifier against demons.
	 */
	public final double getPDefDemons(Creature target) {
		return calcStat(Stats.PDefDemons, 1, target, null);
	}

	/**
	 * @return the Physical Attack range (base+modifier) of the Creature.
	 */
	public int getPhysicalAttackRange() {
		return getActiveChar().getAttackType().getRange();
	}

	/**
	 * @return the ShieldDef rate (base+modifier) of the Creature.
	 */
	public final int getShldDef() {
		return (int) calcStat(Stats.ShieldDefense, 0, null, null);
	}

	/**
	 * @param skill
	 * @return the mpConsume.
	 */
	public final int getMpConsume(L2Skill skill) {
		if (skill == null) {
			return 1;
		}

		double mpConsume = skill.getMpConsume();
		if (skill.isDance()) {
			if (_activeChar != null && _activeChar.getDanceCount() > 0) {
				mpConsume += _activeChar.getDanceCount() * skill.getNextDanceMpCost();
			}
		}

		if (skill.isDance()) {
			return (int) calcStat(Stats.DanceMpConsumeRate, mpConsume, null, null);
		}

		if (skill.isMagic()) {
			return (int) calcStat(Stats.MagicalMpConsumeRate, mpConsume, null, null);
		}

		return (int) calcStat(Stats.PhysicalMpConsumeRate, mpConsume, null, null);
	}

	/**
	 * @param skill
	 * @return the mpInitialConsume.
	 */
	public final int getMpInitialConsume(L2Skill skill) {
		if (skill == null) {
			return 1;
		}

		double mpConsume = skill.getMpInitialConsume();

		if (skill.isDance()) {
			return (int) calcStat(Stats.DanceMpConsumeRate, mpConsume, null, null);
		}

		if (skill.isMagic()) {
			return (int) calcStat(Stats.MagicalMpConsumeRate, mpConsume, null, null);
		}

		return (int) calcStat(Stats.PhysicalMpConsumeRate, mpConsume, null, null);
	}

	public int getAttackElementValue(byte attackAttribute) {
		switch (attackAttribute) {
			case 1: // wind
				return (int) calcStat(Stats.WindPower, 0, null, null);
			case 2: // fire
				return (int) calcStat(Stats.FirePower, 0, null, null);
			case 3: // water
				return (int) calcStat(Stats.WaterPower, 0, null, null);
			case 4: // earth
				return (int) calcStat(Stats.EarthPower, 0, null, null);
			case 5: // holy
				return (int) calcStat(Stats.HolyPower, 0, null, null);
			case 6: // dark
				return (int) calcStat(Stats.DarkPower, 0, null, null);
			default:
				return 0;
		}
	}

	public double getDefenseElementValue(byte defenseAttribute) {
		switch (defenseAttribute) {
			case 1: // wind
				return calcStat(Stats.WindRes, 1, null, null);
			case 2: // fire
				return calcStat(Stats.FireRes, 1, null, null);
			case 3: // water
				return calcStat(Stats.WaterRes, 1, null, null);
			case 4: // earth
				return calcStat(Stats.EarthRes, 1, null, null);
			case 5: // holy
				return calcStat(Stats.HolyRes, 1, null, null);
			case 6: // dark
				return calcStat(Stats.DarkRes, 1, null, null);
			default:
				return 1;
		}
	}

	/**
	 * Returns base running speed, given by owner template.<br>
	 * Player is affected by mount type.
	 *
	 * @return int : Base running speed.
	 */
	public int getBaseRunSpeed() {
		return _activeChar.getTemplate().getBaseRunSpeed();
	}

	/**
	 * Returns base walking speed, given by owner template.<br>
	 * Player is affected by mount type.
	 *
	 * @return int : Base walking speed.
	 */
	public int getBaseWalkSpeed() {
		return _activeChar.getTemplate().getBaseWalkSpeed();
	}

	/**
	 * Returns base movement speed, given by owner template and owner movement
	 * status.<br>
	 * Player is affected by mount type and by being in L2WaterZone.
	 *
	 * @return int : Base walking speed.
	 */
	protected final int getBaseMoveSpeed() {
		return _activeChar.isRunning() ? getBaseRunSpeed() : getBaseWalkSpeed();
	}

	/**
	 * Returns movement speed multiplier, which is used by client to set correct
	 * character/object movement speed.
	 *
	 * @return float : Movement speed multiplier.
	 */
	public final float getMovementSpeedMultiplier() {
		return getMoveSpeed() / getBaseMoveSpeed();
	}

	/**
	 * Returns attack speed multiplier, which is used by client to set correct
	 * character/object attack speed.
	 *
	 * @return float : Attack speed multiplier.
	 */
	public final float getAttackSpeedMultiplier() {
		return (float) ((1.1) * getPAtkSpd() / _activeChar.getTemplate().getBasePAtkSpd());
	}

	/**
	 * Returns final movement speed, given by owner template, owner status and
	 * effects.<br>
	 * L2Playable is affected by L2SwampZone.<br>
	 * Player is affected by L2SwampZone and armor grade penalty.
	 *
	 * @return float : Final movement speed.
	 */
	public float getMoveSpeed() {
		return (float) calcStat(Stats.Speed, getBaseMoveSpeed(), null, null);
	}

	public long getExp() {
		return _exp;
	}

	public void setExp(long value) {
		_exp = value;
	}

	public int getSp() {
		return _sp;
	}

	public void setSp(int value) {
		_sp = value;
	}

	public byte getLevel() {
		return _level;
	}

	public void setLevel(byte value) {
		_level = value;
	}

	public Creature getActiveChar() {
		return _activeChar;
	}

	public int getHpRegenInterval() {
		return (int) calcStat(Stats.HpRegenInterval, 3000, null, null);
	}

	public int getMpRegenInterval() {
		return (int) calcStat(Stats.MpRegenInterval, 3000, null, null);
	}
}
