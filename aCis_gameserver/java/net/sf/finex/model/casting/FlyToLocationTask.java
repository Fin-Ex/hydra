/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.casting;

import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.FlyToLocation;
import net.sf.l2j.gameserver.skills.L2Skill;

@Slf4j
public class FlyToLocationTask implements Runnable {

	private final WorldObject target;
	private final Creature caster;
	private final L2Skill skill;

	public FlyToLocationTask(Creature actor, WorldObject target, L2Skill skill) {
		this.caster = actor;
		this.target = target;
		this.skill = skill;
	}

	@Override
	public void run() {
		try {
			caster.broadcastPacket(new FlyToLocation(caster, target, FlyToLocation.FlyType.valueOf(skill.getFlyType())));
			caster.setXYZ(target.getX(), target.getY(), target.getZ());
		} catch (Exception e) {
			log.error("Failed to execute FlyToLocationTask on {}.", caster, e);
		}
	}

}
