/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 *
 * @author finfan
 */
public class L2SkillAbsorb extends L2Skill {

	public enum AbsorbType {
		FOCUSATION,
	}

	private final AbsorbType absorbType;

	public L2SkillAbsorb(StatsSet set) {
		super(set);
		absorbType = set.getEnum("absorbType", AbsorbType.class);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		switch (absorbType) {
			case FOCUSATION:
				final WorldObject target = targets[0];
				if (!target.isPlayer()) {
					return;
				}

				final Player targetPlayer = target.getPlayer();
				final int charges = targetPlayer.getCharges();
				if (charges > 0) {
					final int restore = (int) (charges * getPower());
					if (targetPlayer == caster) {
						targetPlayer.setCurrentCp(caster.getCurrentCp() + restore);
						targetPlayer.decreaseCharges(charges);
						targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED).addNumber(restore));
					} else {
						if (caster.getMaxCp() > 0) {
							// if caster is player we give him all devoured charges and heal his CP
							caster.setCurrentCp(caster.getCurrentCp() + charges * getPower());
							caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED).addNumber(restore));
						} else {
							// if caster is a monster and target is a player
							caster.setCurrentHp(caster.getCurrentHp() + charges * getPower());
							targetPlayer.sendPacket(SystemMessageId.S1_ABSORBS_YOUR_CHARGES);
						}
						targetPlayer.decreaseCharges(charges);
					}
				}

				break;
				
			default:
				throw new UnsupportedOperationException("Nothing scare, just warning about unhandled absorb type: " + absorbType);
		}
	}

}
