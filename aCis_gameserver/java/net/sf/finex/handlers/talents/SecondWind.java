/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import net.sf.finex.model.classes.Warlord;
import net.sf.finex.model.talents.ITalentHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerHp;

/**
 *
 * @author finfan
 */
public class SecondWind implements ITalentHandler {

	@Override
	public Boolean invoke(Object... args) {
		final Creature creature = (Creature) args[0];
		final L2Skill skill = creature.getSkill(121);
		creature.broadcastPacket(new MagicSkillUse(creature, creature, 4318, 1, 0, 0));
		heal(creature, skill);
		return Boolean.TRUE;
	}

	public static final boolean validate(Creature activeChar) {
		final L2Skill skill = activeChar.getSkill(121);
		if(skill == null || activeChar.isSkillDisabled(skill)) {
			return false;
		}
		
		final Warlord warlord = activeChar.getComponent(Warlord.class);
		if(warlord.hasSecondWindTimeStamp()) {
			return false;
		}
		
		final Env env = new Env();
		env.setCharacter(activeChar);
		env.setTarget(activeChar);
		final boolean test = new ConditionPlayerHp(60).testImpl(env);
		if(!test) {
			return false;
		}
		
		final int seconds = (int) (150.0 / Formulas.CON_BONUS[activeChar.getCON()]);
		activeChar.sendMessage("Talent: `Second Wind` is activated!");
		warlord.getGameObject().sendMessage("Next reuse time: " + seconds + " sec.");
		warlord.setSecondWindTimeStamp(seconds);
		return true;
	}
	
	private void heal(Creature activeChar, L2Skill skill) {
		if (activeChar.isDead()) {
			return;
		}

		double amount = Math.min(activeChar.getMaxHp() * skill.getPower() / 100.0, activeChar.getMaxHp() - activeChar.getCurrentHp());
		activeChar.setCurrentHp(amount + activeChar.getCurrentHp());
		final StatusUpdate su = new StatusUpdate(activeChar);
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED).addNumber((int) amount));
		su.addAttribute(StatusUpdate.CUR_HP, (int) activeChar.getCurrentHp());
		activeChar.sendPacket(su);
	}
}
