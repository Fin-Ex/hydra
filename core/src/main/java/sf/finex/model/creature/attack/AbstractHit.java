/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.creature.attack;

import lombok.Getter;
import sf.l2j.Config;
import sf.l2j.commons.math.MathUtil;
import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.model.ShotType;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import sf.l2j.gameserver.model.actor.events.OnAttack;
import sf.l2j.gameserver.model.actor.instance.Door;
import sf.l2j.gameserver.model.item.kind.Weapon;
import sf.l2j.gameserver.model.item.type.WeaponType;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.Attack;
import sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.skills.Stats;

/**
 *
 * @author FinFan
 */
public abstract class AbstractHit {

	@Getter protected final Creature attacker, target;
	@Getter protected final Weapon weapon;
	@Getter protected int hitTime;
	@Getter protected Attack attack;

	public AbstractHit(Creature attacker, Creature target) {
		this.attacker = attacker;
		this.target = target;
		this.weapon = attacker.getActiveWeaponItem();
	}

	public boolean start() {
		if (target.isDead()) {
			return false;
		}
		
		// Recharge any active auto soulshot tasks for current Creature instance.
		attacker.rechargeShots(true, false);
		hitTime = attacker.getAttackType() == WeaponType.BOW ? 1500 * 345 / attacker.getPAtkSpd() : Formulas.calcPAtkSpd(attacker, target, attacker.getPAtkSpd());
		attacker.setAttackEndTime(System.currentTimeMillis() + hitTime);
		attack = new Attack(attacker, attacker.isChargedShot(ShotType.SOULSHOT), (weapon != null) ? weapon.getCrystalType().getId() : 0);
		attacker.setHeading(MathUtil.calculateHeadingFrom(attacker, target));
		return true;
	}

	public final void hit(DamageInfo info, boolean soulshot) {
		// Deny the whole process if actor is casting.
		if (attacker.isCastingNow()) {
			return;
		}

		// If the attacker/target is dead or use fake death, notify the AI with EVT_CANCEL
		if (target == null || attacker.isAlikeDead()) {
			attacker.getAI().notifyEvent(CtrlEvent.EVT_CANCEL);
			return;
		}

		if ((attacker.isNpc() && target.isAlikeDead()) || target.isDead() || (!attacker.getKnownType(Creature.class).contains(target) && !(attacker instanceof Door))) {
			attacker.getAI().notifyEvent(CtrlEvent.EVT_CANCEL);
			attacker.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (info.isMiss) {
			// Notify target AI
			if (target.hasAI()) {
				target.getAI().notifyEvent(CtrlEvent.EVT_EVADED, attacker);
			}

			// ON_EVADED_HIT
			if (target.getChanceSkills() != null) {
				target.getChanceSkills().onEvadedHit(attacker);
			}

			if (target instanceof Player) {
				target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(attacker));
			}
		}

		if (info.isParry && target.isPlayer()) {
			target.getPlayer().sendPacket(SystemMessageId.PARRY_ATTACK);
		}

		// Send message about damage/crit or miss
		attacker.sendDamageMessage(target, info.damage, false, info.isCrit, info.isMiss, info.isParry);

		// Character will be petrified if attacking a raid that's more than 8 levels lower
		if (!Config.RAID_DISABLE_CURSE && target.isRaid() && attacker.getLevel() > target.getLevel() + 8) {
			final L2Skill skill = SkillTable.FrequentSkill.RAID_CURSE2.getSkill();
			if (skill != null) {
				// Send visual and skill effects. Caster is the victim.
				attacker.broadcastPacket(new MagicSkillUse(attacker, attacker, skill.getId(), skill.getLevel(), 300, 0));
				skill.getEffects(attacker, attacker);
			}

			info.damage = 0; // prevents messing up drop calculation
		}

		// If the target is a player, start AutoAttack
		if (target.isPlayer()) {
			target.getPlayer().getAI().clientStartAutoAttack();
		}

		if (!info.isMiss && info.damage > 0) {
			boolean isBow = (attacker.getAttackType() == WeaponType.BOW);
			int reflectedDamage = 0;

			// Reflect damage system - do not reflect if weapon is a bow or target is invulnerable
			if (!isBow && !target.isInvul()) {
				// quick fix for no drop from raid if boss attack high-level char with damage reflection
				if (!target.isRaid() || attacker.getPlayer() == null || attacker.getPlayer().getLevel() <= target.getLevel() + 8) {
					// Calculate reflection damage to reduce HP of attacker if necessary
					double reflectPercent = target.getStat().calcStat(Stats.ReflectDamPercent, 0, null, null);
					if (reflectPercent > 0) {
						reflectedDamage = (int) (reflectPercent / 100. * info.damage);

						// You can't kill someone from a reflect. If value > current HPs, make damages equal to current HP - 1.
						int currentHp = (int) attacker.getCurrentHp();
						if (reflectedDamage >= currentHp) {
							reflectedDamage = currentHp - 1;
						}
					}
				}
			}

			// Reduce target HPs
			if (!Formulas.calcAbsorb(attacker, target, null)) {
				target.reduceCurrentHp(info.damage, attacker, null);

				// Reduce attacker HPs in case of a reflect.
				if (reflectedDamage > 0) {
					attacker.reduceCurrentHp(reflectedDamage, target, true, false, null);
				}

				if (!isBow) // Do not absorb if weapon is of type bow
				{
					// Absorb HP from the damage inflicted
					double vampirism = attacker.getStat().calcStat(Stats.Vampirism, 0, null, null);

					if (vampirism > 0) {
						int maxCanAbsorb = (int) (attacker.getMaxHp() - attacker.getCurrentHp());
						int absorbDamage = (int) (vampirism / 100. * info.damage);

						if (absorbDamage > maxCanAbsorb) {
							absorbDamage = maxCanAbsorb; // Can't absord more than max hp
						}
						if (absorbDamage > 0) {
							attacker.setCurrentHp(attacker.getCurrentHp() + absorbDamage);
						}
					}
				}

				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(target, info.damage);
			} else {
				if (attacker.isPlayer()) {
					attacker.getPlayer().sendPacket(SystemMessageId.ATTACK_FAILED);
				}
			}

			attacker.getAI().clientStartAutoAttack();

			// Maybe launch chance skills on us
			if (attacker.getChanceSkills() != null) {
				attacker.getChanceSkills().onHit(target, false, info.isCrit);

				// Reflect triggers onHit
				if (reflectedDamage > 0) {
					attacker.getChanceSkills().onHit(target, true, false);
				}
			}

			// Maybe launch chance skills on target
			if (target.getChanceSkills() != null) {
				target.getChanceSkills().onHit(attacker, true, info.isCrit);
			}
		}

		// Launch weapon Special ability effect if available
		final Weapon activeWeapon = attacker.getActiveWeaponItem();
		if (activeWeapon != null) {
			activeWeapon.getSkillEffects(attacker, target, info.isCrit);
		}

		attacker.getEventBus().notify(new OnAttack(attacker, target, info));
	}
}
