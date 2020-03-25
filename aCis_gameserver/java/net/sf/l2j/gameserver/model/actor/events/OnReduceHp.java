/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.events;

import lombok.Data;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
@Data
public class OnReduceHp {
	private final Creature victim, attacker;
	private final L2Skill skill;
	private final double damage;
	private final double remainHp;
	
	public double getRemainHpPercent() {
		return remainHp / victim.getMaxHp() * 100;
	}
}
