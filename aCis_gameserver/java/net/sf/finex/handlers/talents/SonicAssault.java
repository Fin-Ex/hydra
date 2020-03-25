/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import java.util.List;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.finex.model.talents.ITalentHandler;

/**
 *
 * @author finfan
 */
public class SonicAssault implements ITalentHandler {

	@Override
	public Boolean invoke(Object... args) {
		final Player caster = (Player) args[0];

		final List<Creature> list = caster.getKnownTypeInRadius(Creature.class, 150);
		if (list.isEmpty()) {
			return Boolean.FALSE;
		}

		final L2Skill ShockStomp = SkillTable.getInstance().getInfo(452, 5);
		for (Creature around : list) {
			//set stun effect
			final boolean success = Rnd.calcChance(70, 100);
			if (around.isAutoAttackable(caster) && success) {
				ShockStomp.getEffects(caster, around);
			}
		}

		return Boolean.TRUE;
	}

	public static final boolean validate(int id, Player player) {
		return id == 451 && player.hasTalent(SkillTable.FrequentTalent.SONIC_ASSAULT);
	}
}
