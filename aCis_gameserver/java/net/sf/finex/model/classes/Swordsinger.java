/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.classes;

import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.data.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
@Slf4j
public class Swordsinger extends AbstractClassComponent {
	
	public Swordsinger(Player player) {
		super(player);
	}
	
	@Override
	public void onAdd() {
		// add swordsinger skill specialization
		getGameObject().addSkill(FrequentSkill.SINGING_SWORD_MASTERY.getSkill(), false); // not store that
		getGameObject().addSkill(FrequentSkill.MELODY_ARMOR_MASTERY.getSkill(), false); // not store that
		getGameObject().sendSkillList();
	}

	@Override
	public void onRemove() {
		// remove sws skills after removing component
		getGameObject().removeSkill(FrequentSkill.SINGING_SWORD_MASTERY.getSkill());
		getGameObject().removeSkill(FrequentSkill.MELODY_ARMOR_MASTERY.getSkill());
		getGameObject().sendSkillList();
		getGameObject().sendMessage("Remove component of Swordsinger");
	}

	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}
	
	
}
