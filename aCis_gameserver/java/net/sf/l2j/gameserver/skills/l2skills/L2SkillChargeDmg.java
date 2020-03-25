package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.finex.handlers.talents.Challenger;
import net.sf.finex.handlers.talents.RecoiledBlast;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.finex.model.talents.ITalentHandler;

public class L2SkillChargeDmg extends L2Skill {

	public L2SkillChargeDmg(StatsSet set) {
		super(set);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		if (caster.isAlikeDead()) {
			return;
		}

		double modifier = 0;
		double challengerMod = 1.0;
		if (caster.isPlayer()) {
			modifier = 0.7 + 0.3 * caster.getPlayer().getCharges();
			if (Challenger.validate(caster.getPlayer())) {
				challengerMod = ((Challenger) SkillTable.FrequentTalent.CHALLENGER.getHandler()).invoke();
			}
		}

		final boolean ss = caster.isChargedShot(ShotType.SOULSHOT);

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isAlikeDead()) {
				continue;
			}

			// Calculate skill evasion
			boolean skillIsEvaded = Formulas.calcPhysicalSkillEvasion(target, this);
			if (skillIsEvaded) {
				if (caster.isPlayer()) {
					((Player) caster).sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DODGES_ATTACK).addCharName(target));
				}

				if (target.isPlayer()) {
					((Player) target).sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(caster));
				}

				// no futher calculations needed.
				continue;
			}

			final DamageInfo info = new DamageInfo();
			info.shieldResult = Formulas.calcShldUse(caster, target, this);

			if (getBaseCritRate() > 0) {
				info.isCrit = Formulas.calcCrit(getBaseCritRate() * 10 * Formulas.STR_BONUS[caster.getSTR()] * challengerMod);
			}

			info.isParry = Formulas.calcParry(caster, target, this);
			// damage calculation, crit is static 2x
			double damage = Formulas.calcPhysDam(caster, target, this, info, ss);
			if (info.isCrit) {
				damage *= 2;
			}

			if (damage > 0) {
				byte reflect = Formulas.calcSkillReflect(target, this);
				if (hasEffects()) {
					if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0) {
						caster.stopSkillEffects(getId());
						getEffects(target, caster);
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(this));
					} else {
						// activate attacked effects, if any
						target.stopSkillEffects(getId());
						if (Formulas.calcSkillSuccess(caster, target, this, info.shieldResult, true)) {
							getEffects(caster, target, new Env(info.shieldResult, false, false, false));
							target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(this));
						} else {
							caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(this));
						}
					}
				}

				double finalDamage = damage * modifier;
				target.reduceCurrentHp(finalDamage, caster, this);
				
				// vengeance reflected damage
				if ((reflect & Formulas.SKILL_REFLECT_VENGEANCE) != 0) {
					caster.reduceCurrentHp(damage, target, this);
				}

				caster.sendDamageMessage(target, (int) finalDamage, false, info.isCrit, false, info.isParry);
			} else {
				caster.sendDamageMessage(target, 0, false, false, true, info.isParry);
			}
		}

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
