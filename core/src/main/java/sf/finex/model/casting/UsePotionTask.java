/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.casting;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.skills.L2Skill;

/**
 * Task for potion and herb queue
 */
public class UsePotionTask implements Runnable {

	private final Creature caster;
	private final L2Skill skill;

	UsePotionTask(Creature activeChar, L2Skill skill) {
		this.caster = activeChar;
		this.skill = skill;
	}

	@Override
	public void run() {
		caster.doCast(skill, true);
	}
}
