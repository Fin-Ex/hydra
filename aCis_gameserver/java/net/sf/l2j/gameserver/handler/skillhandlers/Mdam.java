package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.finex.enums.ESkillHandlerType;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

public class Mdam implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.MDAM,
		ESkillType.DEATHLINK
	};

	@Override
	public void invoke(Object... args) {
		final Creature caster = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (caster.isAlikeDead()) {
			return;
		}

		final boolean sps = caster.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = caster.isChargedShot(ShotType.BLESSED_SPIRITSHOT);

		for (WorldObject obj : targets) {
			if (!obj.isCreature()) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (caster.isPlayer() && target.isPlayer() && target.getPlayer().isFakeDeath()) {
				target.stopFakeDeath(true);
			} else if (target.isDead()) {
				continue;
			}

			boolean mcrit = Formulas.calcMCrit(caster.getMCriticalHit(target, skill));
			final byte shld = Formulas.calcShldUse(caster, target, skill);
			final byte reflect = Formulas.calcSkillReflect(target, skill);
			final boolean parry = Formulas.calcParry(caster, target, skill);

			int damage = (int) Formulas.calcMagicDam(caster, target, skill, shld, parry, sps, bsps, mcrit);
			if (damage > 0) {
				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(target, damage);

				// vengeance reflected damage
				if ((reflect & Formulas.SKILL_REFLECT_VENGEANCE) != 0) {
					ESkillHandlerType.RETURN_MAGIC.getHandler().invoke(caster, target, skill, damage);
				} else if (target.getFirstEffect(L2EffectType.REDIRECT_SKILL) != null) {
					ESkillHandlerType.REDIRECTION_SKILL.getHandler().invoke(caster.getParams(), target, skill);
					continue;
				} else {
					caster.sendDamageMessage(target, damage, mcrit, false, false, parry);
					target.reduceCurrentHp(damage, caster, skill);
				}

				if (skill.hasEffects()) {
					if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0) // reflect skill effects
					{
						caster.stopSkillEffects(skill.getId());
						skill.getEffects(target, caster);
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(skill));
					} else {
						// activate attacked effects, if any
						target.stopSkillEffects(skill.getId());
						if (Formulas.calcSkillSuccess(caster, target, skill, shld, bsps)) {
							skill.getEffects(caster, target, new Env(shld, sps, false, bsps));
						} else {
							caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill.getId()));
						}
					}
				}
			}
		}

		if (skill.hasSelfEffects()) {
			final L2Effect effect = caster.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect()) {
				effect.exit();
			}

			skill.getEffectsSelf(caster);
		}

		if (skill.isSuicideAttack()) {
			caster.doDie(null);
		}

		caster.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
