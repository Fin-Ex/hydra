/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.casting;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.network.serverpackets.FlyToLocation;
import sf.l2j.gameserver.skills.L2Skill;

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
