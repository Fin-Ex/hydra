/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.skills.l2skills;

import lombok.Getter;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.Func;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 *
 * @author finfan
 */
public class L2SkillBlow extends L2SkillDefault {

	public static final int FRONT = 50;
	public static final int SIDE = 60;
	public static final int BEHIND = 70;
	
	@Getter private final ESkillModifier modifier;
	
	public L2SkillBlow(StatsSet set) {
		super(set);
		modifier = set.getEnum("modifier", ESkillModifier.class, ESkillModifier.NONE);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		if (caster.isAlikeDead()) {
			return;
		}

		final boolean ss = caster.isChargedShot(ShotType.SOULSHOT);

		for (WorldObject obj : targets) {
			if (!obj.isCreature()) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isAlikeDead()) {
				continue;
			}

			byte _successChance = SIDE;

			if (caster.isBehindTarget()) {
				_successChance = BEHIND;
			} else if (caster.isInFrontOfTarget()) {
				_successChance = FRONT;
			}

			// If skill requires Crit or skill requires behind, calculate chance based on DEX, Position and on self BUFF
			boolean success = true;
			if ((getCondition() & L2Skill.COND_BEHIND) != 0) {
				success = (_successChance == BEHIND);
			}
			if ((getCondition() & L2Skill.COND_CRIT) != 0) {
				success = (success && Formulas.calcBlow(caster, target, _successChance));
			}

			if (success) {
				// Calculate skill evasion
				boolean skillIsEvaded = Formulas.calcPhysicalSkillEvasion(target, this);
				if (skillIsEvaded) {
					if (caster.isPlayer()) {
						caster.getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DODGES_ATTACK).addCharName(target));
					}

					if (target.isPlayer()) {
						target.getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(caster));
					}

					// no futher calculations needed.
					continue;
				}

				// Calculate skill reflect
				final byte reflect = Formulas.calcSkillReflect(target, this);
				if (hasEffects()) {
					if (reflect == Formulas.SKILL_REFLECT_SUCCEED) {
						caster.stopSkillEffects(getId());
						getEffects(target, caster);
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(this));
					} else {
						final byte shld = Formulas.calcShldUse(caster, target, this);
						target.stopSkillEffects(getId());
						if (Formulas.calcSkillSuccess(caster, target, this, shld, true)) {
							getEffects(caster, target, new Env(shld, false, false, false));
							target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(this));
						} else {
							caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(this));
						}
					}
				}

				final byte shld = Formulas.calcShldUse(caster, target, this);
				final boolean parry = Formulas.calcParry(caster, target, this);

				// Crit rate base crit rate for skill, modified with STR bonus
				boolean crit = false;
				if (Formulas.calcCrit(getBaseCritRate() * 10 * Formulas.STR_BONUS[caster.getSTR()])) {
					crit = true;
				}

				double damage = (int) Formulas.calcBlowDamage(caster, target, this, shld, parry, ss);
				if (crit) {
					damage *= 2;

					// Vicious Stance is special after C5, and only for BLOW skills
					L2Effect vicious = caster.getFirstEffect(312);
					if (vicious != null && damage > 1) {
						for (Func func : vicious.getStatFuncs()) {
							final Env env = new Env();
							env.setCharacter(caster);
							env.setTarget(target);
							env.setSkill(this);
							env.setValue(damage);

							func.calc(env);
							damage = (int) env.getValue();
						}
					}
				}
				
				
				target.reduceCurrentHp(damage, caster, this);

				// vengeance reflected damage
				if ((reflect & Formulas.SKILL_REFLECT_VENGEANCE) != 0) {
					if (target.isPlayer()) {
						target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COUNTERED_S1_ATTACK).addCharName(caster));
					}

					if (caster.isPlayer()) {
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PERFORMING_COUNTERATTACK).addCharName(target));
					}

					// Formula from Diego post, 700 from rpg tests
					double vegdamage = (700 * target.getPAtk(caster) / caster.getPDef(target));
					caster.reduceCurrentHp(vegdamage, target, this);
				}

				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(target, damage);

				if (caster.isPlayer()) {
					caster.getPlayer().sendDamageMessage(target, (int) damage, false, true, false, parry);
				}
			} else {
				caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
			}

			// Possibility of a lethal strike
			Formulas.calcLethalHit(caster, target, this);

			if (hasSelfEffects()) {
				final L2Effect effect = caster.getFirstEffect(getId());
				if (effect != null && effect.isSelfEffect()) {
					effect.exit();
				}

				getEffectsSelf(caster);
			}
			caster.setChargedShot(ShotType.SOULSHOT, isStaticReuse());
		}
	}
	
	public enum ESkillModifier {
		NONE,
		CASTER_LEVEL
	}
}
