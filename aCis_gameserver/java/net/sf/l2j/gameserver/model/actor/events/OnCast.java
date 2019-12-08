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
 * @author FinFan
 */
@Data
public class OnCast {
	private final Creature caster, target;
	private final L2Skill skill;
}
