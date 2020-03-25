/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items.parts;

import lombok.Data;
import net.sf.finex.model.items.enums.ESpecialAbility;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
@Data
public class PartDataWeapon {

	private WeaponType type;
	private int randomDamage;
	private int SSConsume;
	private int SPSConsume;
	private int MPConsume;
	private int HPConsume;
	private boolean isMagical;
	private IntIntHolder enchantSkill;
	private ESpecialAbility SA;
	private int reuseDelay;
	//private Map<Stats, Double> stats = new HashMap<>();
	
	public void implementSA(Player player) {
		try {
			final L2Skill SAskill = SA.getSkill(player.getLevel());
			if (SAskill != null) {
				player.addSkill(SAskill, false);
				player.sendSkillList();
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void substractSA(Player player) {
		if(player.getSkill(SA.getSkillId()) == null) {
			return;
		}
		
		player.removeSkill(SA.getSkillId());
		player.sendSkillList();
	}
}
