/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.item.kind;

import lombok.Getter;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 *
 * @author finfan
 */
public class SpellItem extends EtcItem {

	@Getter private final IntIntHolder[] spells;
	@Getter private final int multicast;
	
	public SpellItem(StatsSet set) {
		super(set);
		final String[] skills = set.getString("spells", "").split(";");
		if(skills.length > 0) {
			spells = new IntIntHolder[skills.length];
		} else {
			spells = null;
		}
		
		for(int i = 0; i < skills.length; i++) {
			final String[] template = skills[i].split("-");
			final int id = Integer.valueOf(template[0]);
			final int level = Integer.valueOf(template[1]);
			spells[i] = new IntIntHolder(id, level);
		}
		
		multicast = set.getInteger("multicast", 1);
	}

	@Override
	public EtcItemType getItemType() {
		return EtcItemType.SCROLL;
	}
}
