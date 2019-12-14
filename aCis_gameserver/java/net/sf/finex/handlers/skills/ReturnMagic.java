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
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author FinFan
 */
public class ReturnMagic implements ISkillMechanic {

	public static final int REFLECT_TIME = 1000;

	/**
	 * Arguments contract:<br>
	 * <ul>
	 * <li>[0] (Creature) caster</li>
	 * <li>[1] (Creature) reflector</li>
	 * <li>[2] (L2Skill) skill</li>
	 * <li>[3] (double) damage</li>
	 * </ul>
	 *
	 * @param args
	 */
	@Override
	public void invoke(Object... args) {
		final Creature target = (Creature) args[0];
		final Player reflector = (Player) args[1];
		if (reflector.isCastingNow() || reflector.isOutOfControl()
				|| reflector.getParams().getBool(ESkillHandlerType.REDIRECTION_SKILL.name())) {
			return;
		}

		final L2Skill skill = (L2Skill) args[2];
		if (skill.isAoE()) {
			return;
		}

		final int damage = (int) args[3];

		reflector.abortAttack();
		reflector.stopMove(null);
		reflector.setOutOfControl(true);
		final int flyTime = Formulas.calcSkillFlyTime(reflector, target);
		reflector.broadcastPacket(new MagicSkillUse(reflector, target, skill.getId() + 10000, 1, 750, 0));
		reflector.getParams().set(ESkillHandlerType.RETURN_MAGIC.name(), true);

		final int taskTime = flyTime + 400;
		ThreadPool.schedule(() -> {
			target.reduceCurrentHp(damage, reflector, skill);
			reflector.setOutOfControl(false);
			reflector.getParams().remove(ESkillHandlerType.RETURN_MAGIC.name());
		}, taskTime < 750 ? 750 : taskTime);
	}
}
