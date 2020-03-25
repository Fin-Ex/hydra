/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public class GrandfatherClock implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		final L2Skill skill = SkillTable.getInstance().getInfo(1, 1);
		playable.broadcastPacket(new CreatureSay(playable.getObjectId(), Say2.ALL, playable.getName(), "Grandfather's Clock! Stay between time space bitches!"));
		for (Creature creature : playable.getKnownTypeInRadius(Creature.class, 300)) {
			if (creature == playable) {
				continue;
			}

			skill.getEffects(playable, creature);
		}
	}

}
