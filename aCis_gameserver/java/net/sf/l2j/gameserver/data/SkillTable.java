package net.sf.l2j.gameserver.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.sf.finex.handlers.talents.Aftershock;
import net.sf.finex.handlers.talents.AutumnLeafs;
import net.sf.finex.handlers.talents.Challenger;
import net.sf.finex.handlers.talents.CumulativeRage;
import net.sf.finex.handlers.talents.Disaster;
import net.sf.finex.handlers.talents.PowerAbsorption;
import net.sf.finex.handlers.talents.ProfessionalAnger;
import net.sf.finex.handlers.talents.RecoiledBlast;
import net.sf.finex.handlers.talents.SecondWind;
import net.sf.finex.handlers.talents.SonicAssault;
import net.sf.finex.handlers.talents.ThreatIncrease;
import net.sf.finex.handlers.talents.WildHurricane;
import net.sf.finex.model.talents.ITalentHandler;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.skills.DocumentSkill;
import net.sf.l2j.gameserver.skills.L2Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillTable {

	private static final Logger _log = LoggerFactory.getLogger(SkillTable.class.getName());

	private static final Map<Integer, L2Skill> _skills = new HashMap<>();
	private static final Map<Integer, Integer> _skillMaxLevel = new HashMap<>();

	private static final L2Skill[] _heroSkills = new L2Skill[5];
	private static final int[] _heroSkillsId = {
		395,
		396,
		1374,
		1375,
		1376
	};

	private static final L2Skill[] _nobleSkills = new L2Skill[8];
	private static final int[] _nobleSkillsId = {
		325,
		326,
		327,
		1323,
		1324,
		1325,
		1326,
		1327
	};

	public static SkillTable getInstance() {
		return SingletonHolder._instance;
	}

	protected SkillTable() {
		load();
	}

	private void load() {
		final File dir = new File("./data/xml/skills");

		for (File file : dir.listFiles()) {
			final DocumentSkill doc = new DocumentSkill(file);
			doc.parse();
			doc.getSkills().forEach(skill -> _skills.put(getSkillHashCode(skill), skill));
		}

		_log.info("SkillTable: Loaded " + _skills.size() + " skills.");

		// Stores max level of skills in a map for future uses.
		for (final L2Skill skill : _skills.values()) {
			// Only non-enchanted skills
			final int skillLvl = skill.getLevel();
			if (skillLvl < 99) {
				final int skillId = skill.getId();
				final int maxLvl = getMaxLevel(skillId);

				if (skillLvl > maxLvl) {
					_skillMaxLevel.put(skillId, skillLvl);
				}
			}
		}

		// Loading FrequentSkill enumeration values
		for (FrequentSkill sk : FrequentSkill.values()) {
			sk._skill = getInfo(sk._id, sk._level);
		}

		for (int i = 0; i < _heroSkillsId.length; i++) {
			_heroSkills[i] = getInfo(_heroSkillsId[i], 1);
		}

		for (int i = 0; i < _nobleSkills.length; i++) {
			_nobleSkills[i] = getInfo(_nobleSkillsId[i], 1);
		}
	}

	public void reload() {
		_skills.clear();
		_skillMaxLevel.clear();

		load();
	}

	/**
	 * Provides the skill hash
	 *
	 * @param skill The L2Skill to be hashed
	 * @return SkillTable.getSkillHashCode(skill.getId(), skill.getLevel())
	 */
	public static int getSkillHashCode(L2Skill skill) {
		return getSkillHashCode(skill.getId(), skill.getLevel());
	}

	/**
	 * Centralized method for easier change of the hashing sys
	 *
	 * @param skillId The Skill Id
	 * @param skillLevel The Skill Level
	 * @return The Skill hash number
	 */
	public static int getSkillHashCode(int skillId, int skillLevel) {
		return skillId * 256 + skillLevel;
	}

	public L2Skill getInfo(int skillId, int level) {
		return _skills.get(getSkillHashCode(skillId, level));
	}

	public int getMaxLevel(int skillId) {
		final Integer maxLevel = _skillMaxLevel.get(skillId);
		return (maxLevel != null) ? maxLevel : 0;
	}

	public L2Skill getLevelFrom(int skillId, int casterLevel) {
		final int maxLvl = getMaxLevel(skillId);
		if(maxLvl == 1) {
			return getInfo(skillId, 1);
		}

		final int skillLevel = (Experience.MAX_LEVEL - 1) / maxLvl;
		return getInfo(skillId, Math.min(Math.max(casterLevel / skillLevel, 1), maxLvl));
	}

	/**
	 * @param addNoble if true, will add also Advanced headquarters.
	 * @return an array with siege skills.
	 */
	public L2Skill[] getSiegeSkills(boolean addNoble) {
		L2Skill[] temp = new L2Skill[2 + (addNoble ? 1 : 0)];
		int i = 0;

		temp[i++] = _skills.get(SkillTable.getSkillHashCode(246, 1));
		temp[i++] = _skills.get(SkillTable.getSkillHashCode(247, 1));

		if (addNoble) {
			temp[i++] = _skills.get(SkillTable.getSkillHashCode(326, 1));
		}

		return temp;
	}

	public static L2Skill[] getHeroSkills() {
		return _heroSkills;
	}

	public static boolean isHeroSkill(int skillid) {
		for (int id : _heroSkillsId) {
			if (id == skillid) {
				return true;
			}
		}

		return false;
	}

	public static L2Skill[] getNobleSkills() {
		return _nobleSkills;
	}
	
	/**
	 * Enum to hold some important references to frequently used (hardcoded)
	 * skills in core
	 *
	 * @author DrHouse
	 */
	public static enum FrequentSkill {
		LUCKY(194, 1),
		SEAL_OF_RULER(246, 1),
		BUILD_HEADQUARTERS(247, 1),
		STRIDER_SIEGE_ASSAULT(325, 1),
		DWARVEN_CRAFT(1321, 1),
		COMMON_CRAFT(1322, 1),
		LARGE_FIREWORK(2025, 1),
		SPECIAL_TREE_RECOVERY_BONUS(2139, 1),
		ANTHARAS_JUMP(4106, 1),
		ANTHARAS_TAIL(4107, 1),
		ANTHARAS_FEAR(4108, 1),
		ANTHARAS_DEBUFF(4109, 1),
		ANTHARAS_MOUTH(4110, 1),
		ANTHARAS_BREATH(4111, 1),
		ANTHARAS_NORMAL_ATTACK(4112, 1),
		ANTHARAS_NORMAL_ATTACK_EX(4113, 1),
		ANTHARAS_SHORT_FEAR(5092, 1),
		ANTHARAS_METEOR(5093, 1),
		RAID_CURSE(4215, 1),
		WYVERN_BREATH(4289, 1),
		ARENA_CP_RECOVERY(4380, 1),
		RAID_CURSE2(4515, 1),
		VARKA_KETRA_PETRIFICATION(4578, 1),
		FAKE_PETRIFICATION(4616, 1),
		THE_VICTOR_OF_WAR(5074, 1),
		THE_VANQUISHED_OF_WAR(5075, 1),
		BLESSING_OF_PROTECTION(5182, 1),
		FIREWORK(5965, 1),
		SINGING_SWORD_MASTERY(5303, 1),
		MELODY_ARMOR_MASTERY(5304, 1),;

		protected final int _id;
		protected final int _level;
		protected L2Skill _skill = null;

		private FrequentSkill(int id, int level) {
			_id = id;
			_level = level;
		}

		public L2Skill getSkill() {
			return _skill;
		}
	}

	public static enum FrequentTalent {
		DUAL_SWORD_MASTERY(8),
		CUMULATIVE_RAGE(9, new CumulativeRage()),
		SONIC_ASSAULT(10, new SonicAssault()),
		CHALLENGER(11, new Challenger()),
		PROFESSIONAL_ANGER(12, new ProfessionalAnger()),
		RECOILED_BLAST(13, new RecoiledBlast()),
		WILD_HURRICANE(15, new WildHurricane()),
		AFTERSHOCK(16, new Aftershock()),
		THREAT_INCREASE(19, new ThreatIncrease()),
		DISASTER(20, new Disaster()),
		AUTUMN_LEAFS(18, new AutumnLeafs()),
		POWER_ABSOPTION(22, new PowerAbsorption()),
		SECOND_WIND(23, new SecondWind()),
		ENTIRE_HATE(24);
		
		@Getter private final int id;
		@Getter private final ITalentHandler handler;

		private FrequentTalent(int id) {
			this.id = id;
			this.handler = null;
		}

		private FrequentTalent(int id, ITalentHandler handler) {
			this.id = id;
			this.handler = handler;
		}

	}

	private static class SingletonHolder {

		protected static final SkillTable _instance = new SkillTable();
	}
}
