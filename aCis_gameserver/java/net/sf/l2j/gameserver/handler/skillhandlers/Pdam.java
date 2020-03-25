package net.sf.l2j.gameserver.handler.skillhandlers;


import java.util.List;

import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.finex.handlers.talents.CumulativeRage;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;
import net.sf.finex.model.talents.ITalentHandler;

public class Pdam implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.PDAM,
		ESkillType.FATAL
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (activeChar.isAlikeDead()) {
			return;
		}

		int damage = 0;

		final boolean ss = activeChar.isChargedShot(ShotType.SOULSHOT);

		final ItemInstance weapon = activeChar.getActiveWeaponInstance();

		ITalentHandler cumulativeRage = null;
		if (activeChar.isPlayer()) {
			final Player player = activeChar.getPlayer();
			if (CumulativeRage.validate(player)) {
				cumulativeRage = SkillTable.FrequentTalent.CUMULATIVE_RAGE.getHandler();
			}
		}

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (activeChar instanceof Player && target instanceof Player && ((Player) target).isFakeDeath()) {
				target.stopFakeDeath(true);
			} else if (target.isDead()) {
				continue;
			}

			// Calculate skill evasion. As Dodge blocks only melee skills, make an exception with bow weapons.
			if (weapon != null && weapon.getItemType() != WeaponType.BOW && Formulas.calcPhysicalSkillEvasion(target, skill)) {
				if (activeChar instanceof Player) {
					((Player) activeChar).sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DODGES_ATTACK).addCharName(target));
				}

				if (target instanceof Player) {
					((Player) target).sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(activeChar));
				}

				// no futher calculations needed.
				continue;
			}

			final DamageInfo info = new DamageInfo();

			info.shieldResult = Formulas.calcShldUse(activeChar, target, null);
			info.isParry = Formulas.calcParry(activeChar, target, skill);

			// PDAM critical chance not affected by buffs, only by STR. Only some skills are meant to crit.
			if (skill.getBaseCritRate() > 0) {
				info.isCrit = Formulas.calcCrit(skill.getBaseCritRate() * 10 * Formulas.STR_BONUS[activeChar.getSTR()]);
			}

			if (!info.isCrit && (skill.getCondition() & L2Skill.COND_CRIT) != 0) {
				damage = 0;
			} else {
				damage = (int) Formulas.calcPhysDam(activeChar, target, skill, info, ss);
				if (cumulativeRage != null) {
					damage = cumulativeRage.invoke(activeChar.getPlayer(), damage);
				}
			}

			if (info.isCrit) {
				damage *= 2; // PDAM Critical damage always 2x and not affected by buffs
			}
			final byte reflect = Formulas.calcSkillReflect(target, skill);

			if (skill.hasEffects()) {
				List<L2Effect> effects;
				if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0) {
					activeChar.stopSkillEffects(skill.getId());
					effects = skill.getEffects(target, activeChar);
					if (effects != null && !effects.isEmpty()) {
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(skill));
					}
				} else {
					// activate attacked effects, if any
					target.stopSkillEffects(skill.getId());
					effects = skill.getEffects(activeChar, target, new Env(info.shieldResult, false, false, false));
					if (effects != null && !effects.isEmpty()) {
						target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(skill));
					}
				}
			}

			if (damage > 0) {
				activeChar.sendDamageMessage(target, damage, false, info.isCrit, false, info.isParry);

				// Possibility of a lethal strike
				Formulas.calcLethalHit(activeChar, target, skill);

				target.reduceCurrentHp(damage, activeChar, skill);

				// vengeance reflected damage
				if ((reflect & Formulas.SKILL_REFLECT_VENGEANCE) != 0) {
					if (target instanceof Player) {
						target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COUNTERED_S1_ATTACK).addCharName(activeChar));
					}

					if (activeChar instanceof Player) {
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PERFORMING_COUNTERATTACK).addCharName(target));
					}

					double vegdamage = (700 * target.getPAtk(activeChar) / activeChar.getPDef(target));
					activeChar.reduceCurrentHp(vegdamage, target, skill);
				}
			} else {
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
			}
		}

		if (skill.hasSelfEffects()) {
			final L2Effect effect = activeChar.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect()) {
				effect.exit();
			}

			skill.getEffectsSelf(activeChar);
		}

		if (skill.isSuicideAttack()) {
			activeChar.doDie(null);
		}

		activeChar.setChargedShot(ShotType.SOULSHOT, skill.isStaticReuse());
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
