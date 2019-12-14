/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.skills;

import net.sf.finex.enums.ESkillHandlerType;
import net.sf.finex.handlers.ISkillMechanic;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author FinFan
 */
public class RedirectionSkill implements ISkillMechanic {

	/**
	 * Arguments contract:<br>
	 * <ul>
	 * <li>[0] (StatsSet) spellStats (givable by caster of original spell)</li>
	 * <li>[1] (Creature) reflector</li>
	 * <li>[2] (L2Skill) skill</li>
	 * </ul>
	 *
	 * @param args
	 */
	@Override
	public void invoke(Object... args) {
		final StatsSet spellStats = (StatsSet) args[0];
		final Creature reflector = (Creature) args[1];
		final L2Skill skill = (L2Skill) args[2];
		if (reflector.isCastingNow() || reflector.isOutOfControl()
				|| reflector.getParams().getBool(ESkillHandlerType.RETURN_MAGIC.name())) {
			return;
		}

		if (skill.isAoE()) {
			return;
		}

		final Creature victim;
		if (reflector.getTarget() == null) {
			victim = reflector;
		} else {
			victim = (Creature) reflector.getTarget();
		}

		// calc us damage
		final int damage = (int) (Formulas.calcMagicDam(reflector, victim, skill, Formulas.SHIELD_DEFENSE_FAILED, false,
				spellStats.getBool("calcmdam_ss"),
				spellStats.getBool("calcmdam_bss"),
				spellStats.getBool("calcmdam_mcrit")) + spellStats.getDouble("calcmdam_damage")); // modify us damage by original

		if (victim == reflector) {
			// hit self
			victim.sendMessage("You get additional damage cause not controll the power of magic!");
			victim.reduceCurrentHp(damage, reflector, skill);
		} else {
			final Creature target = (Creature) reflector.getTarget();
			reflector.abortAttack();
			reflector.stopMove(null);
			reflector.setOutOfControl(true);
			final int flyTime = Formulas.calcSkillFlyTime(reflector, target);
			reflector.broadcastPacket(new MagicSkillUse(reflector, target, skill.getId() + 20000, 1, ReturnMagic.REFLECT_TIME, 0));
			reflector.getParams().set(ESkillHandlerType.REDIRECTION_SKILL.name(), true);
			ThreadPool.schedule(() -> {
				victim.reduceCurrentHp(damage, reflector, skill);
				reflector.setOutOfControl(false);
				reflector.getParams().remove(ESkillHandlerType.REDIRECTION_SKILL.name());
			}, flyTime + ReturnMagic.REFLECT_TIME);
			reflector.getFirstEffect(L2EffectType.REDIRECT_SKILL).exit();
		}
	}

}
