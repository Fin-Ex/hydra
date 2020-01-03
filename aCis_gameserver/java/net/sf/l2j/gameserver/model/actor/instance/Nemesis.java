/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.model.actor.ai.type.NemesisAI;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 *
 * @author finfan
 */
public class Nemesis extends Monster {
	
	public Nemesis(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public NemesisAI getAI() {
		return (NemesisAI) super.getAI();
	}
}
