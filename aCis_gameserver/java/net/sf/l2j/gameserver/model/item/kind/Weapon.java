package net.sf.l2j.gameserver.model.item.kind;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.scripting.EventType;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.basefuncs.FuncTemplate;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.conditions.ConditionGameChance;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * This class is dedicated to the management of weapons.
 */
@Slf4j
public final class Weapon extends Item {

	private final WeaponType _type;
	private final int _rndDam;
	private final int _soulShotCount;
	private final int _spiritShotCount;
	private final int _mpConsume, _hpConsume;
	private final boolean _isMagical;

	private IntIntHolder _enchant4Skill = null; // skill that activates when item is enchanted +4 (for duals)

	// Attached skills for Special Abilities
	private IntIntHolder skillOnCastStatic;
	private int skillOnCastDynamic;
	private Condition skillOnCastCondition = null;

	private IntIntHolder skillsOnCritStatic;
	private int skillOnCritDynamic;
	private Condition skillsOnCritCondition = null;

	private final int _reuseDelay;

	private final int _reducedSoulshot;
	private final int _reducedSoulshotChance;
	@Getter
	private Map<Stats, Double> stats = new HashMap<>();

	public Weapon(StatsSet set) {
		super(set);
		_type = WeaponType.valueOf(set.getString("weapon_type", "none").toUpperCase());
		_type1 = Item.TYPE1_WEAPON_RING_EARRING_NECKLACE;
		_type2 = Item.TYPE2_WEAPON;
		_soulShotCount = set.getInteger("soulshots", 0);
		_spiritShotCount = set.getInteger("spiritshots", 0);
		_rndDam = set.getInteger("random_damage", 0);
		_mpConsume = set.getInteger("mp_consume", 0);
		_hpConsume = set.getInteger("hp_consume", 0);
		_reuseDelay = set.getInteger("reuse_delay", 0);
		_isMagical = set.getBool("is_magical", false);

		String[] reduced_soulshots = set.getString("reduced_soulshot", "").split(",");
		_reducedSoulshotChance = (reduced_soulshots.length == 2) ? Integer.parseInt(reduced_soulshots[0]) : 0;
		_reducedSoulshot = (reduced_soulshots.length == 2) ? Integer.parseInt(reduced_soulshots[1]) : 0;

		String skill = set.getString("enchant4_skill", null);
		if (skill != null) {
			String[] info = skill.split("-");

			if (info != null && info.length == 2) {
				int id = 0;
				int level = 0;
				try {
					id = Integer.parseInt(info[0]);
					level = Integer.parseInt(info[1]);
				} catch (NumberFormatException e) {
					log.error("Couldnt parse {} in weapon enchant skills! item {}", skill, this, e);
				}
				if (id > 0 && level > 0) {
					_enchant4Skill = new IntIntHolder(id, level);
				}
			}
		}

		skill = set.getString("oncast_skill", null);
		if (skill != null) {
			final String[] splitter = skill.split("-");
			final int id = Integer.parseInt(splitter[0]);
			if (splitter.length > 1) {
				final int level = Integer.parseInt(splitter[1]);
				skillOnCastStatic = new IntIntHolder(id, level);
			} else {
				skillOnCastDynamic = id;
			}
			String infochance = set.getString("oncast_chance", null);
			if (infochance != null) {
				skillOnCastCondition = new ConditionGameChance(Integer.parseInt(infochance));
			}
		} else {
			skillOnCastDynamic = -1;
		}

		skill = set.getString("oncrit_skill", null);
		if (skill != null) {
			final String[] splitter = skill.split("-");
			final int id = Integer.parseInt(splitter[0]);
			if (splitter.length > 1) {
				final int level = Integer.parseInt(splitter[1]);
				skillsOnCritStatic = new IntIntHolder(id, level);
			} else {
				skillOnCritDynamic = id;
			}
			String infochance = set.getString("oncrit_chance", null);
			if (infochance != null) {
				skillsOnCritCondition = new ConditionGameChance(Integer.parseInt(infochance));
			}
		} else {
			skillOnCritDynamic = -1;
		}
	}

	/**
	 * @return the type of weapon.
	 */
	@Override
	public WeaponType getItemType() {
		return _type;
	}

	/**
	 * @return the ID of the Etc item after applying the mask.
	 */
	@Override
	public int getItemMask() {
		return getItemType().mask();
	}

	/**
	 * @return the quantity of SoulShot used.
	 */
	public int getSoulShotCount() {
		return _soulShotCount;
	}

	/**
	 * @return the quatity of SpiritShot used.
	 */
	public int getSpiritShotCount() {
		return _spiritShotCount;
	}

	/**
	 * @return the reduced quantity of SoultShot used.
	 */
	public int getReducedSoulShot() {
		return _reducedSoulshot;
	}

	/**
	 * @return the chance to use Reduced SoultShot.
	 */
	public int getReducedSoulShotChance() {
		return _reducedSoulshotChance;
	}

	/**
	 * @return the random damage inflicted by the weapon
	 */
	public int getRandomDamage() {
		return _rndDam;
	}

	/**
	 * @return the Reuse Delay of the Weapon.
	 */
	public int getReuseDelay() {
		return _reuseDelay;
	}

	/**
	 * @return true or false if weapon is considered as a mage weapon.
	 */
	public final boolean isMagical() {
		return _isMagical;
	}

	/**
	 * @return the MP consumption of the weapon.
	 */
	public int getMpConsume(Creature owner) {
		return (int) owner.getStat().calcStat(Stats.AttackMpConsumeRate, _mpConsume, owner, null);
	}

	public int getMpConsume() {
		return _mpConsume;
	}
	
	public int getHpConsume(Creature owner) {
		return (int) owner.getStat().calcStat(Stats.AttackHpConsumeRate, _hpConsume, owner, null);
	}

	public int getHpConsume() {
		return _hpConsume;
	}
	
	/**
	 * @return The skill player obtains when he equiped weapon +4 or more (for
	 * duals SA)
	 */
	public L2Skill getEnchant4Skill() {
		if (_enchant4Skill == null) {
			return null;
		}

		return _enchant4Skill.getSkill();
	}

	public IntIntHolder getEnchant4Holder() {
		if (_enchant4Skill == null) {
			return null;
		}

		return _enchant4Skill;
	}

	/**
	 * @param caster : Creature pointing out the caster
	 * @param target : Creature pointing out the target
	 * @param crit : boolean tells whether the hit was critical
	 * @return An array of L2Effect of skills associated with the item to be
	 * triggered onHit.
	 */
	public List<L2Effect> getSkillEffects(Creature caster, Creature target, boolean crit) {
		if (!crit) {
			return Collections.emptyList();
		}

		L2Skill skill = null;
		if (skillsOnCritStatic != null) {
			skill = skillsOnCritStatic.getSkill();
		} else if (skillOnCritDynamic > 0) {
			skill = SkillTable.getInstance().getLevelFrom(skillOnCritDynamic, caster.getLevel());
		}

		if (skill == null) {
			return Collections.emptyList();
		}

		final List<L2Effect> effects = new ArrayList<>();

		if (skillsOnCritCondition != null) {
			final Env env = new Env();
			env.setCharacter(caster);
			env.setTarget(target);
			env.setSkill(skill);

			if (!skillsOnCritCondition.test(env)) {
				return Collections.emptyList();
			}
		}

		final byte shld = Formulas.calcShldUse(caster, target, skill);
		if (!Formulas.calcSkillSuccess(caster, target, skill, shld, false)) {
			return Collections.emptyList();
		}

		if (target.getFirstEffect(skill.getId()) != null) {
			target.getFirstEffect(skill.getId()).exit();
		}

		for (L2Effect e : skill.getEffects(caster, target, new Env(shld, false, false, false))) {
			effects.add(e);
		}

		return effects;
	}

	/**
	 * @param caster : Creature pointing out the caster
	 * @param target : Creature pointing out the target
	 * @param trigger : L2Skill pointing out the skill triggering this action
	 * @return An array of L2Effect associated with the item to be triggered
	 * onCast.
	 */
	public List<L2Effect> getSkillEffects(Creature caster, Creature target, L2Skill trigger) {
		L2Skill skillOnCast = null;
		if (skillOnCastStatic != null) {
			skillOnCast = skillOnCastStatic.getSkill();
		} else if (skillOnCastDynamic > 0) {
			skillOnCast = SkillTable.getInstance().getLevelFrom(skillOnCastDynamic, caster.getLevel());
		}

		if (skillOnCast == null) {
			return Collections.emptyList();
		}

		// Trigger only same type of skill.
		if (trigger.isOffensive() != skillOnCast.isOffensive()) {
			return Collections.emptyList();
		}

		// No buffing with toggle or not magic skills.
		if ((trigger.isToggle() || !trigger.isMagic()) && skillOnCast.getSkillType() == ESkillType.BUFF) {
			return Collections.emptyList();
		}

		if (skillOnCastCondition != null) {
			final Env env = new Env();
			env.setCharacter(caster);
			env.setTarget(target);
			env.setSkill(skillOnCast);

			if (!skillOnCastCondition.test(env)) {
				return Collections.emptyList();
			}
		}

		final byte shld = Formulas.calcShldUse(caster, target, skillOnCast);
		if (skillOnCast.isOffensive() && !Formulas.calcSkillSuccess(caster, target, skillOnCast, shld, false)) {
			return Collections.emptyList();
		}

		Creature[] targets = new Creature[1];
		targets[0] = target;

		// Get the skill handler corresponding to the skill type
		final IHandler handler = HandlerTable.getInstance().get(skillOnCast.getSkillType());
		if (handler != null) {
			handler.invoke(caster, skillOnCast, targets);
		} else {
			skillOnCast.useSkill(caster, targets);
		}

		// notify quests of a skill use
		if (caster.isPlayer()) {
			// Mobs in range 1000 see spell
			for (Npc npcMob : caster.getKnownTypeInRadius(Npc.class, 1000)) {
				List<Quest> quests = npcMob.getTemplate().getEventQuests(EventType.ON_SKILL_SEE);
				if (quests != null) {
					for (Quest quest : quests) {
						quest.notifySkillSee(npcMob, (Player) caster, skillOnCast, targets, false);
					}
				}
			}
		}
		return Collections.emptyList();
	}

	@Override
	public void attach(FuncTemplate f) {
		super.attach(f);
		// insert only static BASE stats
		if (f.order == 0x08) {
			stats.put(f.stat, ((LambdaConst) f.lambda).getValue());
		}
	}
}
